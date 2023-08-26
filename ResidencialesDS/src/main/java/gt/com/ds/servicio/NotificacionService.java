package gt.com.ds.servicio;

import gt.com.ds.domain.Notificacion;
import java.util.List;

/**
 *
 * @author cjbojorquez
 */
public interface NotificacionService {
    
    public List<Notificacion> notificacionPorTipo(String tipo,Long idResidencial);
    
    public List<Notificacion> notificacionPorEstado(String tipo,Long idEstado,Long idResidencial);
    
    public List<Notificacion> notificacionPorUsuario(String tipo,Long idUsuario);
    
    public List<Notificacion> listarNotificacionesAbiertas(String tipo,Long idResidencial);
    
    public void guardar(Notificacion notificacion);
    
    public void eliminar(Notificacion notificacion);
    
    public Notificacion encontrarNotificacion(Notificacion notificacion);
}
