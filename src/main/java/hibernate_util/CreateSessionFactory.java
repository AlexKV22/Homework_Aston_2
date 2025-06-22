package hibernate_util;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class CreateSessionFactory {
    private static SessionFactory sessionFactoryReady;
    static {
        try {
            sessionFactoryReady = new Configuration().addAnnotatedClass(model.User.class).buildSessionFactory();
        } catch(HibernateException e) {
            // WRITE LOGGING!!
        }
    }

    private CreateSessionFactory() {}

    public static SessionFactory getSessionFactory() {
        return sessionFactoryReady;
    }

    public static void shutdownFactory() {
        sessionFactoryReady.close();
    }
}
