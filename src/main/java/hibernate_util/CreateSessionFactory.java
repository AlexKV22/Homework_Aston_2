package hibernate_util;

import exception.NoSuccessCreateSessionFactoryException;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CreateSessionFactory {
    private static final Logger logger = Logger.getLogger(CreateSessionFactory.class.getName());
    private static SessionFactory sessionFactoryReady;

    private static SessionFactory initSessionFactory() {
        SessionFactory sessionFactory = null;
        try {
            sessionFactory = new Configuration().addAnnotatedClass(model.User.class).buildSessionFactory();
            logger.info("Создание SessionFactory завершилось успешно");
            return sessionFactory;
        } catch(HibernateException e) {
            logger.log(Level.WARNING,"Не удалось создать SessionFactory", e);
            throw new NoSuccessCreateSessionFactoryException("Не удалось создать SessionFactory");
        }
    }

    private CreateSessionFactory() {}

    public static SessionFactory getSessionFactory() {
        sessionFactoryReady = initSessionFactory();
        return sessionFactoryReady;
    }

    public static void shutdownFactory() {
        sessionFactoryReady.close();
    }
}
