package myApp.service;

import myApp.dto.dtoResponse.UserResponseDto;
import myApp.exception.NoDeleteUserException;
import myApp.exception.NoSaveNewUserException;
import myApp.exception.NoUpdateUserException;
import myApp.exception.UserNotFoundException;
import myApp.model.User;
import myApp.repository.dto.UserRepositoryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = Logger.getLogger(UserServiceImpl.class.getName());
    private final UserRepositoryDto userRepositoryDto;

    @Autowired
    public UserServiceImpl(UserRepositoryDto userRepositoryDto) {
        this.userRepositoryDto = userRepositoryDto;
    }

    @Override
    @Transactional
    public UserResponseDto create(User user) {
        try {
            user.setCreated_at(Date.valueOf(LocalDate.now()));
            UserResponseDto userResponseDto = userRepositoryDto.create(user);
            logger.info("Создание user завершилось успешно");
            return userResponseDto;
        } catch (DataAccessException e) {
            logger.log(Level.WARNING,"Не удалось сохранить нового user", e);
            throw new NoSaveNewUserException("Не удалось сохранить нового user", e);
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
                logger.log(Level.WARNING, String.format("Не удалось найти юзера с id = %s", id));
                throw new UserNotFoundException(String.format("Не удалось найти юзера с id = %s", id));
            }
        } catch (DataAccessException e) {
            logger.log(Level.WARNING,"Не удалось обновить user", e);
            throw new NoUpdateUserException("Не удалось обновить user", e);
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
            } else {
                logger.log(Level.WARNING, String.format("Не удалось найти юзера с id = %s", id));
                throw new UserNotFoundException(String.format("Не удалось найти юзера с id = %s", id));
            }
        } catch (DataAccessException e) {
            logger.log(Level.WARNING,"Не удалось удалить user", e);
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
            logger.log(Level.WARNING, String.format("Не удалось найти юзера с id = %s", id));
            throw new UserNotFoundException(String.format("Не удалось найти юзера с id = %s", id));
        }
    }
}
