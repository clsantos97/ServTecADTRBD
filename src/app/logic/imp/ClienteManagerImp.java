/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.logic.imp;

import app.config.db.DbManager;
import app.logic.interfaces.ClienteManager;
import app.logic.pojo.ClienteBean;
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
public class ClienteManagerImp implements ClienteManager {

    private static final Logger logger = Logger.getLogger(ClienteManagerImp.class.getName());
    private static DbManager db = DbManager.getDbConn();

    @Override
    public Collection<ClienteBean> getClientes() {
        List<ClienteBean> clientes = new ArrayList<>();

        try {

            ResultSet rs = db.query("SELECT * FROM cliente");
            while (rs.next()) {
                clientes.add(new ClienteBean(rs.getInt("id"), rs.getString("name"),
                        rs.getString("lastname"), rs.getString("phone"),
                        rs.getString("email"), rs.getString("zipcode")));
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(ClienteManagerImp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return clientes;
    }

    @Override
    public boolean deleteCliente(ClienteBean cliente) {
        boolean res = false;
        Connection dbConnection = null;
        PreparedStatement preparedStatement = null;

        String insertTableSQL = "DELETE FROM cliente WHERE id = ?";

        try {
            dbConnection = db.getConn();
            preparedStatement = dbConnection.prepareStatement(insertTableSQL);

            preparedStatement.setInt(1, cliente.getId());

            // execute insert SQL stetement
            preparedStatement.executeUpdate();
            res = true;
            logger.log(Level.INFO, "Cliente <{0}> eliminado.", cliente.getName());

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Ha ocurrido un error al eliminar el cliente: {0}", e.getMessage());
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
    public boolean updateCliente(ClienteBean cliente) {
        boolean res = false;
        Connection dbConnection = null;
        PreparedStatement preparedStatement = null;

        String insertTableSQL = "UPDATE cliente "
                + "SET name = ?, lastname = ?, phone = ?, email = ?, zipcode = ? "
                + "WHERE id = ?;";

        try {
            dbConnection = db.getConn();
            preparedStatement = dbConnection.prepareStatement(insertTableSQL);

            preparedStatement.setString(1, cliente.getName());
            preparedStatement.setString(2, cliente.getLastname());
            preparedStatement.setString(3, cliente.getPhone());
            preparedStatement.setString(4, cliente.getEmail());
            preparedStatement.setString(5, cliente.getZipcode());
            preparedStatement.setInt(6, cliente.getId());

            // execute insert SQL stetement
            preparedStatement.executeUpdate();
            res = true;
            logger.log(Level.INFO, "Cliente <{0}> modificado.", cliente.getName());

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Ha ocurrido un error al modificar el cliente: {0}", e.getMessage());
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
    public boolean insertCliente(ClienteBean cliente) {
        boolean res = false;
        Connection dbConnection = null;
        PreparedStatement preparedStatement = null;

        String insertTableSQL = "INSERT INTO cliente"
                + "(name, lastname, phone, email, zipcode) VALUES"
                + "(?,?,?,?,?)";

        try {
            dbConnection = db.getConn();
            preparedStatement = dbConnection.prepareStatement(insertTableSQL);

            preparedStatement.setString(1, cliente.getName());
            preparedStatement.setString(2, cliente.getLastname());
            preparedStatement.setString(3, cliente.getPhone());
            preparedStatement.setString(4, cliente.getEmail());
            preparedStatement.setString(5, cliente.getZipcode());

            // execute insert SQL stetement
            preparedStatement.executeUpdate();
            res = true;
            logger.log(Level.INFO, "Cliente <{0}> modificado.", cliente.getName());

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Ha ocurrido un error al modificar el cliente: {0}", e.getMessage());
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
    public ClienteBean getClienteById(Integer id) {
        ClienteBean cliente = null;

        Connection dbConnection = null;
        PreparedStatement preparedStatement = null;

        String insertTableSQL = "SELECT * FROM cliente WHERE id = ? LIMIT 1";

        try {
            dbConnection = db.getConn();
            preparedStatement = dbConnection.prepareStatement(insertTableSQL);

            preparedStatement.setInt(1, id);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                cliente = new ClienteBean(rs.getInt("id"), rs.getString("name"),
                        rs.getString("lastname"), rs.getString("phone"), rs.getString("email"), rs.getString("zipcode"));
            }

            logger.log(Level.INFO, "Cliente <{0}> modificado.", cliente.getName());

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Ha ocurrido un error al modificar el cliente: {0}", e.getMessage());
        }

        return cliente;
    }

}
