/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.logic.imp;

import app.config.db.HibernateUtil;
import app.logic.interfaces.FacturaManager;
import app.logic.pojo.ClienteBean;
import app.logic.pojo.FacturaBean;
import app.logic.pojo.ServicioBean;
import app.model.ClienteEntity;
import app.model.FacturaEntity;
import app.model.ServicioEntity;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author Carlos Santos
 */
public class FacturaManagerImp implements FacturaManager {

    private static final Logger logger = Logger.getLogger(FacturaManagerImp.class.getName());

    @Override
    public Collection<FacturaBean> getFacturas() {
        logger.log(Level.INFO, "Getting all facturas");
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<FacturaBean> facturaList = new ArrayList();

        try {
            List<FacturaEntity> facturaEntities = session.createQuery("from FacturaEntity").list();
            if (facturaEntities != null) {
                facturaEntities.forEach(f
                        -> facturaList.add(new FacturaBean(f.getId(),
                                new ClienteBean(f.getCliente().getId(), f.getCliente().getName(),
                                        f.getCliente().getLastname(), f.getCliente().getPhone(), f.getCliente().getEmail(),
                                        f.getCliente().getZipcode()),
                                entityToDTO(f.getServicios()), f.getDate(), f.getTotal())));
            }

        } catch (HibernateException e) {
            logger.log(Level.SEVERE, "An error has ocurred while getting all facturas");
        } finally {
            session.close();
        }
        return facturaList;
    }

    @Override
    public boolean deleteFactura(FacturaBean factura) {
        logger.log(Level.INFO, "Deleting factura <{0}>.", factura.getId());
        boolean res = false;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        String hql = "from FacturaEntity where id = :id";

        try {
            tx = session.beginTransaction();
            Query query = session.createQuery(hql);
            query.setParameter("id", factura.getId());
            FacturaEntity facturaToDelete = (FacturaEntity) query.uniqueResult();

            if (facturaToDelete != null) {
                session.delete(facturaToDelete);

                logger.log(Level.INFO, "Factura id<{0} deleted", factura.getId());
            } else {
                logger.log(Level.INFO, "Factura id<{0}> not found.", factura.getId());
            }
            tx.commit();
            res = true;
            logger.log(Level.INFO, "Factura {0} deleted.", factura.getId());
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            logger.severe("An error has ocurred while deleting factura <" + factura.getId() + ">:");
        } finally {
            session.close();
        }
        return res;
    }

    @Override
    public boolean updateFactura(FacturaBean factura) {
        logger.log(Level.INFO, "Updating factura <{0}>.", factura.getId());
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        boolean res = false;
        String hql = "from FacturaEntity where id = :id";

        try {
            tx = session.beginTransaction();

            // Retrieve ingredient to update
            Query query = session.createQuery(hql);
            query.setParameter("id", factura.getId());
            FacturaEntity facturaToUpdate = (FacturaEntity) query.uniqueResult();

            if (facturaToUpdate != null) {
                // Update ingredient attributes
                facturaToUpdate.setCliente(getClienteEntityById(factura.getCliente().getId()));
                facturaToUpdate.setDate(factura.getDate());
                facturaToUpdate.setServicios(dtoToEntity(factura.getServicios()));
                facturaToUpdate.setTotal(factura.getTotal());
   

                // Update servicio in db
                session.update(facturaToUpdate);
            }

            tx.commit();
            res = true;
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            logger.log(Level.SEVERE, "An error has ocurred while updating factura<{0}>:", factura.getId());
        } finally {
            session.close();
        }
        return res;
    }

    @Override
    public boolean insertFactura(FacturaBean factura) {
        logger.log(Level.INFO, "Creating factura for cliente<{0}>.", factura.getCliente().getId());
        boolean res = false;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try {
        FacturaEntity facturaEntity = new FacturaEntity(getClienteEntityById(factura.getCliente().getId()),
                    factura.getDate(),
                    dtoToEntity(factura.getServicios()),
                    factura.getTotal());
        
            tx = session.beginTransaction();
            session.save(facturaEntity);
            tx.commit();
            res = true;
            logger.log(Level.INFO, "factura created.");
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            logger.severe("An error has ocurred while creating factura");
        } finally {
            session.close();
        }
        return res;
    }

    public List<ServicioEntity> dtoToEntity(List<ServicioBean> servicios) {
        logger.log(Level.INFO, "Getting servicio entities from a list of servicio dto.");
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<ServicioEntity> filteredServicios = new ArrayList<>();

        if (servicios != null) {
            List<Integer> servicioIds = new ArrayList<Integer>();
            servicios.forEach(i -> servicioIds.add(i.getId()));

            try {
                List<ServicioEntity> servicioEntities = session.createQuery("from ServicioEntity").list();

                filteredServicios = servicioEntities.stream().filter(i -> servicioIds.contains(i.getId())).collect(Collectors.toList());

            } catch (HibernateException e) {
                logger.log(Level.SEVERE, "An error has ocurred while getting servicio entities from dtos");
            } finally {
                session.close();
            }
        }

        return filteredServicios;
    }

    public List<ServicioBean> entityToDTO(List<ServicioEntity> ingredients) {
        logger.log(Level.INFO, "Getting ingredient dtos from a list of ingredient entities.");
        List<ServicioBean> servicioDtoList = new ArrayList<>();

        if (ingredients != null) {
            ingredients.forEach(i -> servicioDtoList.add(new ServicioBean(i.getId(), i.getName(), i.getDescription(), i.getPrice())));
        }

        return servicioDtoList;
    }
    
    public ClienteEntity getClienteEntityById(Integer id){
        logger.log(Level.INFO, "Getting cliente entity by id<{0}>", String.valueOf(id));

        ClienteEntity clienteResult = null;

        if (id != 0) {
            Session session = HibernateUtil.getSessionFactory().openSession();
            try {
                clienteResult = (ClienteEntity) session.get(ClienteEntity.class, id);
            } catch (HibernateException e) {
                logger.log(Level.SEVERE, "An error has ocurred while getting cliente <{0}>:", String.valueOf(id));
            } finally {
                session.close();
            }
        }
        return clienteResult;
    }
}
