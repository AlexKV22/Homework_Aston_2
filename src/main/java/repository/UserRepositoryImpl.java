package repository;

import hibernate_util.CreateSessionFactory;
import model.User;
import org.hibernate.SessionFactory;

public class UserRepositoryImpl implements UserRepository {
    private final SessionFactory sessionFactory = CreateSessionFactory.getSessionFactory();

    @Override
    public void create(User user) {

    }

    @Override
    public void update(User user) {

    }

    @Override
    public void delete(int id) {

    }

    @Override
    public User read(int id) {
        return null;
    }
}
