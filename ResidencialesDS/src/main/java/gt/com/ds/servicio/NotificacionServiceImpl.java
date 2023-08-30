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
    
    @Override
    public List<Notificacion> notificacionPorTipo(String tipo, Long idResidencial) {
        return (List<Notificacion>)notificacionDao.buscarPorTipo(tipo,idResidencial);
    }

    @Override
    public List<Notificacion> notificacionPorEstado(String tipo,Long idEstado, Long idResidencial) {
        return (List<Notificacion>)notificacionDao.buscarPorEstado(tipo,idEstado,idResidencial);
    }

    @Override
    public List<Notificacion> notificacionPorUsuario(String tipo,Long idUsuario) {
        return (List<Notificacion>)notificacionDao.buscarPorUsuario(tipo,idUsuario);
    }

    @Override
    public List<Notificacion> listarNotificacionesAbiertas(String tipo,Long idResidencial) {
        return (List<Notificacion>)notificacionDao.buscarActivos(tipo,idResidencial);
    }

    @Override
    public Notificacion guardar(Notificacion notificacion) {
        return notificacionDao.save(notificacion);
    }

    @Override
    public void eliminar(Notificacion notificacion) {
        notificacionDao.delete(notificacion);
    }

    @Override
    public Notificacion encontrarNotificacion(Notificacion notificacion) {
        return notificacionDao.findById(notificacion.getIdNotificacion()).orElse(null);
    }
    
}
