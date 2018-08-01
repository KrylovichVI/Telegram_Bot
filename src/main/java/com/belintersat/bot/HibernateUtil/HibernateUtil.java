package com.belintersat.bot.HibernateUtil;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

/**
 * Created by KrylovichVI on 01.08.2018.
 */
public class HibernateUtil {
    private static SessionFactory sessionFactory;

    private static SessionFactory createSessionFactory(){
        Configuration configuration = new Configuration().configure();
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
