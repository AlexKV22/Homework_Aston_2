package service;

import dao.DAO;
import dao.DAOHibernateImpl;
import exception.NoDeleteUserException;
import exception.NoSaveNewUserException;
import exception.NoUpdateUserException;
import model.User;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserServiceImpl implements UserService {
    private static final Logger logger = Logger.getLogger(UserServiceImpl.class.getName());
    private final DAO dao;

    public UserServiceImpl() {
        this.dao = new DAOHibernateImpl();
    }
    public UserServiceImpl(DAO dao) {
        this.dao = dao;
    }

    @Override
    public void create(String name, String email, Integer age) {
        User user = new User(name, email, age, Date.valueOf(LocalDate.now()));
        try {
            dao.create(user);
            logger.info("Создание user завершилось успешно");
        } catch (NoSaveNewUserException e) {
            logger.log(Level.WARNING,"Не удалось сохранить нового user", e);
        } catch (NullPointerException e) {
            logger.log(Level.WARNING,"Не удалось сохранить нового user, user null", e);
        }
    }

    @Override
    public void update(String name, String email, Integer age, User user) {
        user.setName(name);
        user.setEmail(email);
        user.setAge(age);
        try {
            dao.update(user);
            logger.info("Обновление user завершилось успешно");
        } catch (NoUpdateUserException e) {
            logger.log(Level.WARNING,"Не удалось обновить user", e);
        }
    }

    @Override
    public void delete(Long id) {
        try {
            dao.delete(id);
            logger.info("Удаление user завершилось успешно");
        } catch (NoDeleteUserException e) {
            logger.log(Level.WARNING,"Не удалось удалить user", e);
        }
    }

    @Override
    public User read(Long id) {
        Optional<User> read = dao.read(id);
        if (read.isPresent()) {
            logger.info("Чтение user завершилось успешно");
            return read.get();
        } else {
            logger.warning(String.format("Не найден user c id: %s", id));
            return null;
        }
    }
}
