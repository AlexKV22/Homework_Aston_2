package myApp.service;

import myApp.dao.DAO;
import myApp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    private final DAO dao;

    @Autowired
    public UserServiceImpl(@Qualifier("DAO") DAO dao) {
        this.dao = dao;
    }

    @Override
    @Transactional
    public boolean create(String name, String email, Integer age) {
        User user = new User(name, email, age, Date.valueOf(LocalDate.now()));
        try {
            if (user == null) {
                throw new NullPointerException("Не удалось сохранить нового user, user null");
            } else {
                dao.save(user);
                logger.info("Создание user завершилось успешно");
                return true;
            }
        } catch (DataAccessException e) {
            logger.log(Level.WARNING,"Не удалось сохранить нового user", e);
            return false;
        } catch (NullPointerException e) {
            logger.log(Level.WARNING,"Не удалось сохранить нового user, user null", e);
            return false;
        }
    }

    @Override
    @Transactional
    public boolean update(String name, String email, Integer age, User user) {
        user.setName(name);
        user.setEmail(email);
        user.setAge(age);
        try {
            dao.save(user);
            logger.info("Обновление user завершилось успешно");
            return true;
        } catch (DataAccessException e) {
            logger.log(Level.WARNING,"Не удалось обновить user", e);
        }
        return false;
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        try {
            dao.deleteById(id);
            logger.info("Удаление user завершилось успешно");
            return true;
        } catch (DataAccessException e) {
            logger.log(Level.WARNING,"Не удалось удалить user", e);
        }
        return false;
    }

    @Override
    @Transactional
    public User read(Long id) {
        Optional<User> read = dao.findById(id);
        if (read.isPresent()) {
            logger.info("Чтение user завершилось успешно");
            return read.get();
        } else {
            logger.warning(String.format("Не найден user c id: %s", id));
            return null;
        }
    }
}
