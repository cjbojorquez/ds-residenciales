package gt.com.ds.servicio;

import gt.com.ds.dao.NotificacionDao;
import gt.com.ds.domain.Notificacion;
import gt.com.ds.domain.Ticket;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author cjbojorquez
 */
@Service
public class NotificacionServiceImpl implements NotificacionService{

    @Autowired
    private NotificacionDao notificacionDao;
    
    /**
     * Esta función lista las notificaciones por tipo y por residencial
     * @param tipo
     * @param idResidencial
     * @return 
     */
    @Override
    @Transactional(readOnly = true)
    public List<Notificacion> notificacionPorTipo(String tipo, Long idResidencial) {
        return (List<Notificacion>)notificacionDao.buscarPorTipo(tipo,idResidencial);
    }

    /**
     * ESta funcion lista todas las notificaciones por tipo, por estado y por residencial
     * @param tipo
     * @param idEstado
     * @param idResidencial
     * @return 
     */
    @Override
    @Transactional(readOnly = true)
    public List<Notificacion> notificacionPorEstado(String tipo,Long idEstado, Long idResidencial) {
        return (List<Notificacion>)notificacionDao.buscarPorEstado(tipo,idEstado,idResidencial);
    }

    /**
     * Esta función lista las notificaciones por tipo y por usuario
     * @param tipo
     * @param idUsuario
     * @return 
     */
    @Override
    @Transactional(readOnly = true)
    public List<Notificacion> notificacionPorUsuario(String tipo,Long idUsuario) {
        return (List<Notificacion>)notificacionDao.buscarPorUsuario(tipo,idUsuario);
    }

    /**
     * Esta función lista las notificaciones abiertas por tipo y por residencial
     * @param tipo
     * @param idResidencial
     * @return 
     */
    @Override
    @Transactional(readOnly = true)
    public List<Notificacion> listarNotificacionesAbiertas(String tipo,Long idResidencial) {
        return (List<Notificacion>)notificacionDao.buscarActivos(tipo,idResidencial);
    }

    /**
     * Esta función permite guardar una notificación en base de datos
     * @param notificacion
     * @return 
     */
    @Override
    @Transactional
    public Notificacion guardar(Notificacion notificacion) {
        return notificacionDao.save(notificacion);
    }

    @Override
    @Transactional
    public void eliminar(Notificacion notificacion) {
        notificacionDao.delete(notificacion);
    }

    /**
     * Esta función nos permite ubicar una notificación buscandola por el objeto
     * @param notificacion
     * @return 
     */
    @Override
    @Transactional(readOnly = true)
    public Notificacion encontrarNotificacion(Notificacion notificacion) {
        return notificacionDao.findById(notificacion.getIdNotificacion()).orElse(null);
    }
    
    
}
