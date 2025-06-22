package service;

import dao.DAO;
import dao.DAOHibernateImpl;
import model.User;
import org.example.App;

import java.time.LocalDate;
import java.util.Optional;
import java.util.logging.Logger;

public class UserServiceImpl implements UserService {
    private static final Logger logger = Logger.getLogger(UserServiceImpl.class.getName());
    private final DAO dao;

    public UserServiceImpl() {
        this.dao = new DAOHibernateImpl();
    }

    @Override
    public void create(String name, String email, Integer age) {
        User user = new User(name, email, age, LocalDate.now());
        dao.create(user);
    }

    @Override
    public void update(String name, String email, Integer age, User user) {
        user.setName(name);
        user.setEmail(email);
        user.setAge(age);
        dao.update(user);
    }

    @Override
    public void delete(Long id) {
        dao.delete(id);
    }

    @Override
    public User read(Long id) {
        Optional<User> read = dao.read(id);
        if (read.isPresent()) {
            return read.get();
        } else {
            logger.warning(String.format("Не найден user c id: %s", id));
            return null;
        }
    }
}
