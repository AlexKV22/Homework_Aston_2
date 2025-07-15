package myApp.service;

import myApp.dto.dtoResponse.UserResponseDto;
import myApp.exception.NoDeleteUserException;
import myApp.exception.NoSaveNewUserException;
import myApp.exception.NoUpdateUserException;
import myApp.exception.UniqueFieldException;
import myApp.exception.UserNotFoundException;
import myApp.model.User;
import myApp.repository.dto.UserRepositoryDto;
import myApp.userTempKafka.UserTempKafka;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepositoryDto userRepositoryDto;
    private final KafkaTemplate<String, UserTempKafka> kafkaTemplate;

    @Autowired
    public UserServiceImpl(UserRepositoryDto userRepositoryDto, KafkaTemplate<String, UserTempKafka> kafkaTemplate) {
        this.userRepositoryDto = userRepositoryDto;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    @Transactional
    public UserResponseDto create(User user) {
        try {
            user.setCreated_at(Date.valueOf(LocalDate.now()));
            UserResponseDto userResponseDto = userRepositoryDto.create(user);
            logger.info("Создание user завершилось успешно");
            UserTempKafka userTempKafka = new UserTempKafka();
            userTempKafka.setEmail(user.getEmail());
            userTempKafka.setCreateOrDelete("Create");
            kafkaTemplate.send("test-topic", userTempKafka);
            logger.debug("Продюсер успешно отправил в Kafka сообщение о создании юзера");
            return userResponseDto;
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
    public UserResponseDto update(User user, Long id) {
        try {
            Optional<User> byId = userRepositoryDto.findById(id);
            if (byId.isPresent()) {
                user.setId(id);
                user.setCreated_at(byId.get().getCreated_at());
                logger.info("Обновление user завершилось успешно");
                return userRepositoryDto.update(user);
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
            Optional<User> byId = userRepositoryDto.findById(id);
            if (byId.isPresent()) {
                userRepositoryDto.deleteById(id);
                logger.info("Удаление user завершилось успешно");
                UserTempKafka userTempKafka = new UserTempKafka();
                userTempKafka.setEmail(byId.get().getEmail());
                userTempKafka.setCreateOrDelete("Delete");
                kafkaTemplate.send("test-topic", userTempKafka);
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
    public UserResponseDto read(Long id) {
        Optional<User> readUser = userRepositoryDto.findById(id);
        if (readUser.isPresent()) {
            UserResponseDto responseDto = userRepositoryDto.getResponseDto(readUser.get());
            logger.info("Чтение user завершилось успешно");
            return responseDto;
        } else {
            logger.warn("Не удалось найти юзера с id = {}", id);
            throw new UserNotFoundException(String.format("Не удалось найти юзера с id = %s", id));
        }
    }
}
