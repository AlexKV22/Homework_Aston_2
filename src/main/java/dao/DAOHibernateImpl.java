package dao;

import hibernate_util.CreateSessionFactory;
import model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Optional;
import java.util.logging.Logger;

public class DAOHibernateImpl implements DAO {
    private final SessionFactory sessionFactory = CreateSessionFactory.getSessionFactory();
    private Session currentSession;
    private static final Logger logger = Logger.getLogger(DAOHibernateImpl.class.getName());

    private Session getCurrentSession() {
        if (currentSession == null) {
            currentSession = sessionFactory.openSession();
            logger.info("Сессия Hibernate создана успешно");
        }
        return currentSession;
    }

    @Override
    public void create(User user) {
        Session session = getCurrentSession();
        session.beginTransaction();
        session.persist(user);
        session.getTransaction().commit();
        if (!session.getTransaction().isActive()) {
            logger.info("Транзакция создания user завершилась успешно");
        }
        session.close();
    }

    @Override
    public void update(User user) {
        Session session = getCurrentSession();
        session.beginTransaction();
        session.merge(user);
        session.getTransaction().commit();
        if (!session.getTransaction().isActive()) {
            logger.info("Транзакция изменения user завершилась успешно");
        }
        session.close();
    }

    @Override
    public void delete(Long id) {
        Session session = getCurrentSession();
        session.beginTransaction();
        session.remove(session.get(User.class, id));
        session.getTransaction().commit();
        if (!session.getTransaction().isActive()) {
            logger.info("Транзакция удаления user завершилась успешно");
        }
        session.close();
    }

    @Override
    public Optional<User> read(Long id) {
        User user = getCurrentSession().get(User.class, id);
        return Optional.ofNullable(user);
    }
}
