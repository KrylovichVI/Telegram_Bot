package com.belintersat.bot.HibernateUtil;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


public class HibernateUtil {
    private static SessionFactory sessionFactory;
    private static SessionFactory createSessionFactory(){
        Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
       // StandardServiceRegistry registryBuilder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();

        SessionFactory sessionFactory = configuration.buildSessionFactory();

        return sessionFactory;
    }

    public static SessionFactory getInstance(){
        if(sessionFactory == null){
            sessionFactory = createSessionFactory();
        }
        return sessionFactory;
    }

}
