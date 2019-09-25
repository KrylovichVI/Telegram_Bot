package com.belintersat.bot.Domain;

import com.belintersat.bot.HibernateUtil.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

/**
 * Created by KrylovichVI on 01.08.2018.
 */
public class FlagDAO implements DAO {
    @Override
    public void addFlag(Flags flag) {
        Session session = HibernateUtil.getInstance().openSession();
        session.beginTransaction();
        session.save(flag);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void updateFlag(Flags flags) {
        Session session = HibernateUtil.getInstance().openSession();
        session.beginTransaction();
        session.saveOrUpdate(flags);
        session.getTransaction().commit();
        session.close();
    }


    @Override
    public Flags getFlagById(long flag_id) {
        Session session = HibernateUtil.getInstance().openSession();
        session.beginTransaction();
        Flags flags = session.load(Flags.class, flag_id);
        session.getTransaction().commit();
        session.close();
        return flags;
    }

    @Override
    public Flags getFlagByName(String name) {
        Session session = HibernateUtil.getInstance().openSession();
        Query query = session.createQuery("from Flags as fg where fg.name = :fgName");
        query.setParameter("fgName",name);
        Flags flag =(Flags)query.getSingleResult();
        session.close();
        return flag;
    }

    @Override
    public List<Flags> getAllFlags() {
        Session session = HibernateUtil.getInstance().openSession();
        session.beginTransaction();
        Query query = session.createQuery("from Flags ");
        List<Flags> resultList = query.getResultList();
        session.getTransaction().commit();
        session.close();
        return resultList;
    }

    @Override
    public void deleteFlag(Flags flags) {
        Session session = HibernateUtil.getInstance().openSession();
        session.beginTransaction();
        session.delete(flags);
        session.getTransaction().commit();
        session.close();
    }
}
