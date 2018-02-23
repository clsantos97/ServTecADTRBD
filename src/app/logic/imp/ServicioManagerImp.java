/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.logic.imp;

import app.config.db.DbManager;
import app.gui.controller.HomeController;
import app.logic.interfaces.ServicioManager;
import app.logic.pojo.ServicioBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Carlos Santos
 */
public class ServicioManagerImp implements ServicioManager {

    private static final Logger logger = Logger.getLogger(ServicioManagerImp.class.getName());
    private static DbManager db = DbManager.getDbConn();

    @Override
    public Collection<ServicioBean> getServicios() {
        List<ServicioBean> servicios = new ArrayList<>();

        try {

            ResultSet rs = db.query("SELECT * FROM servicio");
            while (rs.next()) {
                servicios.add(new ServicioBean(rs.getInt("id"), rs.getString("name"),
                        rs.getString("description"), rs.getDouble("price")));
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(ServicioManagerImp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return servicios;
    }

    @Override
    public boolean deleteServicio(ServicioBean servicio) {
        boolean res = false;
        Connection dbConnection = null;
        PreparedStatement preparedStatement = null;

        String insertTableSQL = "DELETE FROM servicio WHERE id = ?";

        try {
            dbConnection = db.getConn();
            preparedStatement = dbConnection.prepareStatement(insertTableSQL);

            preparedStatement.setInt(1, servicio.getId());

            // execute insert SQL stetement
            preparedStatement.executeUpdate();
            res = true;
            logger.log(Level.INFO, "Servicio <{0}> eliminado.", servicio.getName());

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Ha ocurrido un error al eliminar el servicio: {0}", e.getMessage());
        } finally {

//            try {
//                if (preparedStatement != null) {
//                    preparedStatement.close();
//                }
//                if (dbConnection != null) {
//                    dbConnection.close();
//                }
//            } catch (SQLException ex) {
//                Logger.getLogger(ServicioManagerImp.class.getName()).log(Level.SEVERE, null, ex);
//            }
        }
        return res;
    }

    @Override
    public boolean updateServicio(ServicioBean servicio) {
        boolean res = false;
        Connection dbConnection = null;
        PreparedStatement preparedStatement = null;

        String insertTableSQL = "UPDATE servicio "
                + "SET name = ?, description = ?, price = ? "
                + "WHERE id = ?;";

        try {
            dbConnection = db.getConn();
            preparedStatement = dbConnection.prepareStatement(insertTableSQL);

            //preparedStatement.setInt(1, servicio.getId());
            preparedStatement.setString(1, servicio.getName());
            preparedStatement.setString(2, servicio.getDescription());
            preparedStatement.setDouble(3, servicio.getPrice());
            preparedStatement.setInt(4, servicio.getId());

            // execute insert SQL stetement
            preparedStatement.executeUpdate();
            res = true;
            logger.log(Level.INFO, "Servicio <{0}> modificado.", servicio.getName());

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Ha ocurrido un error al modificar el servicio: {0}", e.getMessage());
        } finally {

//            try {
//                if (preparedStatement != null) {
//                    preparedStatement.close();
//                }
//                if (dbConnection != null) {
//                    dbConnection.close();
//                }
//            } catch (SQLException ex) {
//                Logger.getLogger(ServicioManagerImp.class.getName()).log(Level.SEVERE, null, ex);
//            }
        }
        return res;
    }

    @Override
    public boolean insertServicio(ServicioBean servicio) {

        boolean res = false;
        Connection dbConnection = null;
        PreparedStatement preparedStatement = null;

        String insertTableSQL = "INSERT INTO servicio"
                + "(name, description, price) VALUES"
                + "(?,?,?)";

        try {
            dbConnection = db.getConn();
            preparedStatement = dbConnection.prepareStatement(insertTableSQL);

            preparedStatement.setString(1, servicio.getName());
            preparedStatement.setString(2, servicio.getDescription());
            preparedStatement.setDouble(3, servicio.getPrice());

            // execute insert SQL stetement
            preparedStatement.executeUpdate();
            res = true;
            logger.log(Level.INFO, "Servicio <{0}> creado.", servicio.getName());

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Ha ocurrido un error al crear el servicio: {0}", e.getMessage());
        } finally {

//            try {
//                if (preparedStatement != null) {
//                    preparedStatement.close();
//                }
//                if (dbConnection != null) {
//                    dbConnection.close();
//                }
//            } catch (SQLException ex) {
//                Logger.getLogger(ServicioManagerImp.class.getName()).log(Level.SEVERE, null, ex);
//            }
        }

        return res;
    }

    @Override
    public Collection<ServicioBean> getServiciosByFactura(Integer id) {
        List<ServicioBean> servicios = new ArrayList<>();

        try {

            ResultSet rs = db.query("SELECT * FROM ");
            while (rs.next()) {
                servicios.add(new ServicioBean(rs.getInt("id"), rs.getString("name"),
                        rs.getString("description"), rs.getDouble("price")));
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(ServicioManagerImp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return servicios;
    }

}
