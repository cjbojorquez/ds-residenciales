package gt.com.ds.servicio;

import gt.com.ds.dao.SolicitudServicioDao;
import gt.com.ds.domain.SolicitudServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author cjbojorquez
 */
@Service
public class SolicitudServicioServiceImpl implements SolicitudServicioService{

    @Autowired
    private SolicitudServicioDao servicioDao;
    
    @Override
    @Transactional(readOnly = true)
    public List<SolicitudServicio> listarSolicitudes(Long estado, Long idResidencial) {
        
        return (List<SolicitudServicio>)servicioDao.buscarPorEstado(estado,idResidencial);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SolicitudServicio> listarPorUsuario(Long estado, Long idUsuario) {
        
        return (List<SolicitudServicio>)servicioDao.buscarPorUsuario(estado,idUsuario);
    }
    
    @Override
    @Transactional
    public void guardar(SolicitudServicio solicitudServicio) {
        servicioDao.save(solicitudServicio);
    }

    @Override
    @Transactional
    public void eliminar(SolicitudServicio solicitudServicio) {
        servicioDao.delete(solicitudServicio);
    }

    
    @Override
    @Transactional(readOnly = true)
    public List<SolicitudServicio> listarServicios() {
        return (List<SolicitudServicio>)servicioDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public SolicitudServicio encontrarServicio(SolicitudServicio solicitudServicio) {
        return servicioDao.findById(solicitudServicio.getIdSolicitud()).orElse(null);
    }
    
}
