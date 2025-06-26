package dao;

import exception.NoDeleteUserException;
import exception.NoSaveNewUserException;
import exception.NoUpdateUserException;
import exception.NotSuccessOpenSessionException;
import hibernate_util.CreateSessionFactory;
import model.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DAOHibernateImpl implements DAO {
    private final SessionFactory sessionFactory;
    private Session currentSession;
    private static final Logger logger = Logger.getLogger(DAOHibernateImpl.class.getName());

    public DAOHibernateImpl() {
        this.sessionFactory = CreateSessionFactory.getSessionFactory();
    }
    public DAOHibernateImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getCurrentSession() {
        if (currentSession == null) {
            try {
                currentSession = sessionFactory.openSession();
                logger.info("Сессия Hibernate создана успешно");
            } catch (HibernateException e) {
                logger.log(Level.WARNING,"Не удалось установить сессию", e);
                throw new NotSuccessOpenSessionException("Не удалось установить сессию");
            }
        }
        return currentSession;
    }

    @Override
    public boolean create(User user) throws NoSaveNewUserException {
        Session session = null;
        try {
            if (user != null) {
                session = getCurrentSession();
                session.beginTransaction();
                session.persist(user);
                session.getTransaction().commit();
                return true;
            } else {
                throw new NullPointerException("Не удалось сохранить нового user, user null");
            }
        } catch (HibernateException e) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            throw new NoSaveNewUserException("Не удалось сохранить нового user", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public boolean update(User user) throws NoUpdateUserException {
        Session session = null;
        try{
            session = getCurrentSession();
            session.beginTransaction();
            session.merge(user);
            session.getTransaction().commit();
            return true;
        } catch (HibernateException e) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            throw new NoUpdateUserException("Не удалось обновить данные user", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public boolean delete(Long id) throws NoDeleteUserException {
        Session session = null;
        try {
            session = getCurrentSession();
            session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                session.remove(user);
                session.getTransaction().commit();
                return true;
            } else {
                throw new NoDeleteUserException("Не удалось удалить user, user null");
            }
        } catch (HibernateException e) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            throw new NoDeleteUserException("Не удалось удалить user", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Optional<User> read(Long id) {
        User user = getCurrentSession().get(User.class, id);
        return Optional.ofNullable(user);
    }
}
