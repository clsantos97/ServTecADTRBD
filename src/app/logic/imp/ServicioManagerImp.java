/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.logic.imp;

import app.config.db.HibernateUtil;
import app.logic.interfaces.ServicioManager;
import app.logic.pojo.ServicioBean;
import app.model.ServicioEntity;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author Carlos Santos
 */
public class ServicioManagerImp implements ServicioManager {

    private static final Logger logger = Logger.getLogger(ServicioManagerImp.class.getName());

    @Override
    public Collection<ServicioBean> getServicios() {
        logger.log(Level.INFO, "Getting all servicios");
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<ServicioBean> servicioList = new ArrayList();

        try {
            List<ServicioEntity> servicioEntities = session.createQuery("from ServicioEntity").list();
            if (servicioEntities != null) {
                servicioEntities.forEach(s
                        -> servicioList.add(new ServicioBean(s.getId(), s.getName(), s.getDescription(), s.getPrice())));
            }

        } catch (HibernateException e) {
            logger.log(Level.SEVERE, "An error has ocurred while getting all servicios");
        } finally {
            session.close();
        }
        return servicioList;
    }

    @Override
    public boolean deleteServicio(ServicioBean servicio) {
        logger.log(Level.INFO, "Deleting servicio <{0}>.", servicio.getName());
        boolean res = false;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        String hql = "from ServicioEntity where id = :id";

        try {
            tx = session.beginTransaction();
            Query query = session.createQuery(hql);
            query.setParameter("id", servicio.getId());
            ServicioEntity servicioToDelete = (ServicioEntity) query.uniqueResult();

            if (servicioToDelete != null) {
                session.delete(servicioToDelete);

                logger.log(Level.INFO, "Servicio id<{0}>, name<{1}> deleted.", new Object[]{servicio.getId(), servicio.getName()});
            } else {
                logger.log(Level.INFO, "Servicio id<{0}> not found.", servicio.getId());
            }
            tx.commit();
            res = true;
            logger.log(Level.INFO, "Servicio {0} deleted.", servicio.getName());
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            logger.severe("An error has ocurred while deleting servicio <" + servicio.getName() + ">:");
        } finally {
            session.close();
        }
        return res;
    }

    @Override
    public boolean updateServicio(ServicioBean servicio) {
        logger.log(Level.INFO, "Updating servicio <{0}>.", servicio.getId());
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        boolean res = false;
        String hql = "from ServicioEntity where id = :id";

        try {
            tx = session.beginTransaction();

            // Retrieve ingredient to update
            Query query = session.createQuery(hql);
            query.setParameter("id", servicio.getId());
            ServicioEntity servicioToUpdate = (ServicioEntity) query.uniqueResult();

            if (servicioToUpdate != null) {
                // Update ingredient attributes
                servicioToUpdate.setName(servicio.getName());
                servicioToUpdate.setDescription(servicio.getDescription());
                servicioToUpdate.setPrice(servicio.getPrice());
   

                // Update servicio in db
                session.update(servicioToUpdate);
            }

            tx.commit();
            res = true;
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            logger.log(Level.SEVERE, "An error has ocurred while updating servicio<{0}>:", servicio.getId());
        } finally {
            session.close();
        }
        return res;
    }

    @Override
    public boolean insertServicio(ServicioBean servicio) {
        logger.log(Level.INFO, "Creating servicio <{0}>.", servicio.getName());
        boolean res = false;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            session.save(new ServicioEntity(
                    servicio.getName(),
                    servicio.getDescription(),
                    servicio.getPrice()
            ));
            tx.commit();
            res = true;
            logger.log(Level.INFO, "Servicio {0} created.", servicio.getName());
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            logger.severe("An error has ocurred while creating servicio <" + servicio.getName() + ">:");
        } finally {
            session.close();
        }
        return res;
    }

}
