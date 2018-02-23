/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.logic.interfaces;

import app.logic.pojo.ServicioBean;
import java.util.Collection;

/**
 *
 * @author Carlos
 */
public interface ServicioManager {
    public Collection<ServicioBean> getServicios();
    public boolean deleteServicio(ServicioBean servicio);
    public boolean updateServicio(ServicioBean servicio);
    public boolean insertServicio(ServicioBean servicio);
    public Collection<ServicioBean> getServiciosByFactura(Integer id);
}
