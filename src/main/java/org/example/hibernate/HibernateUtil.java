package org.example.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static final SessionFactory sessionFactory;

    static {
        try {
            Configuration configuration = new Configuration()
                    .configure("hibernate.cfg.xml")
                    .addAnnotatedClass(org.example.model.Rental.class)
                    .addAnnotatedClass(org.example.model.Vehicle.class)
                    .addAnnotatedClass(org.example.model.Motorcycle.class)
                    .addAnnotatedClass(org.example.model.Car.class)
                    .addAnnotatedClass(org.example.model.User.class);

            sessionFactory = configuration.buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError("Błąd inicjalizacji Hibernate: " + ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        getSessionFactory().close();
    }
}