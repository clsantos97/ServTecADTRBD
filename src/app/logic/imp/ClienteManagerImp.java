/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.logic.imp;

import app.config.db.HibernateUtil;
import app.logic.interfaces.ClienteManager;
import app.logic.pojo.ClienteBean;
import app.model.ClienteEntity;
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
public class ClienteManagerImp implements ClienteManager {

    private static final Logger logger = Logger.getLogger(ClienteManagerImp.class.getName());

    @Override
    public Collection<ClienteBean> getClientes() {
        logger.log(Level.INFO, "Getting all clientes");
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<ClienteBean> clienteList = new ArrayList();

        try {
            List<ClienteEntity> clienteEntities = session.createQuery("from ClienteEntity").list();
            if (clienteEntities != null) {
                clienteEntities.forEach(c
                        -> clienteList.add(new ClienteBean(c.getId(), c.getName(), c.getLastname(), c.getPhone(), c.getEmail(), c.getZipcode())));
            }

        } catch (HibernateException e) {
            logger.log(Level.SEVERE, "An error has ocurred while getting all clientes");
        } finally {
            session.close();
        }
        return clienteList;
    }

    @Override
    public boolean deleteCliente(ClienteBean cliente) {
        logger.log(Level.INFO, "Deleting cliente <{0}>.", cliente.getName());
        boolean res = false;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        String hql = "from ClienteEntity where id = :id";

        try {
            tx = session.beginTransaction();
            Query query = session.createQuery(hql);
            query.setParameter("id", cliente.getId());
            ClienteEntity clienteToDelete = (ClienteEntity) query.uniqueResult();

            if (clienteToDelete != null) {
                session.delete(clienteToDelete);

                logger.log(Level.INFO, "Cliente id<{0}>, name<{1}> deleted.", new Object[]{cliente.getId(), cliente.getName()});
            } else {
                logger.log(Level.INFO, "Cliente id<{0}> not found.", cliente.getId());
            }
            tx.commit();
            res = true;
            logger.log(Level.INFO, "Cliente {0} deleted.", cliente.getName());
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            logger.severe("An error has ocurred while deleting cliente <" + cliente.getName() + ">:");
        } finally {
            session.close();
        }
        return res;
    }

    @Override
    public boolean updateCliente(ClienteBean cliente) {
        logger.log(Level.INFO, "Updating cliente <{0}>.", cliente.getId());
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        boolean res = false;
        String hql = "from ClienteEntity where id = :id";

        try {
            tx = session.beginTransaction();

            // Retrieve ingredient to update
            Query query = session.createQuery(hql);
            query.setParameter("id", cliente.getId());
            ClienteEntity clienteToUpdate = (ClienteEntity) query.uniqueResult();

            if (clienteToUpdate != null) {
                // Update ingredient attributes
                clienteToUpdate.setName(cliente.getName());
                clienteToUpdate.setLastname(cliente.getLastname());
                clienteToUpdate.setPhone(cliente.getPhone());
                clienteToUpdate.setEmail(cliente.getEmail());
                clienteToUpdate.setZipcode(cliente.getZipcode());

                // Update ingredient in db
                session.update(clienteToUpdate);
            }

            tx.commit();
            res = true;
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            logger.log(Level.SEVERE, "An error has ocurred while updating cliente<{0}>:", cliente.getId());
        } finally {
            session.close();
        }
        return res;
    }

    @Override
    public boolean insertCliente(ClienteBean cliente) {
        logger.log(Level.INFO, "Creating cliente <{0}>.", cliente.getName());
        boolean res = false;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            session.save(new ClienteEntity(
                    cliente.getName(),
                    cliente.getLastname(),
                    cliente.getPhone(),
                    cliente.getEmail(),
                    cliente.getZipcode()
            ));
            tx.commit();
            res = true;
            logger.log(Level.INFO, "Cliente {0} created.", cliente.getName());
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            logger.severe("An error has ocurred while creating cliente <" + cliente.getName() + ">:");
        } finally {
            session.close();
        }
        return res;
    }

}
