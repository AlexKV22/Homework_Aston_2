package myApp.service;

import myApp.converter.UserMapper;
import myApp.dto.dtoRequest.UserRequestDto;
import myApp.dto.dtoResponse.UserResponseDto;
import myApp.exception.NoDeleteUserException;
import myApp.exception.NoSaveNewUserException;
import myApp.exception.NoUpdateUserException;
import myApp.exception.UniqueFieldException;
import myApp.exception.UserNotFoundException;
import myApp.exception.UserNotReadException;
import myApp.kafkaSender.KafkaSender;
import myApp.model.User;
import myApp.repository.UserRepository;
import myApp.utils.StatusSendKafka;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final KafkaSender kafkaSender;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, KafkaSender kafkaSender) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.kafkaSender = kafkaSender;
    }

    @Override
    @Transactional
    public UserResponseDto create(UserRequestDto userRequestDto) {
        try {
            User user = userMapper.dtoToEntity(userRequestDto);
            user.setCreated_at(Date.valueOf(LocalDate.now()));
            User saveUser = userRepository.save(user);
            logger.info("Создание user завершилось успешно");
            kafkaSender.sendingKafka(saveUser, StatusSendKafka.CREATE);
            logger.debug("Продюсер успешно отправил в Kafka сообщение о создании юзера");
            return userMapper.entityToDto(saveUser);
        } catch (DataAccessException e) {
            logger.warn("Не удалось сохранить нового user", e);
            throw new NoSaveNewUserException("Не удалось сохранить нового user", e);
        } catch (ConstraintViolationException e) {
            logger.warn("Ошибка создания юзера, этот email занят.", e);
            throw new UniqueFieldException("Ошибка создания юзера, этот email занят.", e);
        }
    }

    @Override
    @Transactional
    public UserResponseDto update(UserRequestDto userRequestDto, Long id) {
        try {
            User user = userMapper.dtoToEntity(userRequestDto);
            Optional<User> byId = userRepository.findById(id);
            if (byId.isPresent()) {
                user.setId(id);
                user.setCreated_at(byId.get().getCreated_at());
                User updateUser = userRepository.save(user);
                logger.info("Обновление user завершилось успешно");
                return userMapper.entityToDto(updateUser);
            } else {
                logger.warn("Не удалось найти юзера с id = {}", id);
                throw new UserNotFoundException(String.format("Не удалось найти юзера с id = %s", id));
            }
        } catch (DataAccessException e) {
            logger.warn("Не удалось обновить user", e);
            throw new NoUpdateUserException("Не удалось обновить user", e);
        } catch (ConstraintViolationException e) {
            logger.warn("Ошибка обновления юзера, этот email занят.", e);
            throw new UniqueFieldException("Ошибка обновления юзера, этот email занят.", e);
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        try {
            Optional<User> byId = userRepository.findById(id);
            if (byId.isPresent()) {
                userRepository.deleteById(id);
                logger.info("Удаление user завершилось успешно");
                kafkaSender.sendingKafka(byId.get(), StatusSendKafka.DELETE);
                logger.debug("Продюсер успешно отправил в Kafka сообщение об удалении юзера");
            } else {
                logger.warn("Не удалось найти юзера с id = {}", id);
                throw new UserNotFoundException(String.format("Не удалось найти юзера с id = %s", id));
            }
        } catch (DataAccessException e) {
            logger.warn("Не удалось удалить user", e);
            throw new NoDeleteUserException("Не удалось удалить user", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public User read(Long id) {
        try {
            Optional<User> readUser = userRepository.findById(id);
            if (readUser.isPresent()) {
                logger.info("Чтение user завершилось успешно");
                return readUser.get();
            } else {
                logger.warn("Не удалось найти юзера с id = {}", id);
                throw new UserNotFoundException(String.format("Не удалось найти юзера с id = %s", id));
            }
        } catch (DataAccessException e) {
            logger.warn("Не удалось прочитать user, ошибка на сервере", e);
            throw new UserNotReadException("Не удалось прочитать user, ошибка на сервере", e);
        }
    }
}
