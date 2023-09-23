package gt.com.ds.servicio;

import gt.com.ds.dao.ServicioDao;
import gt.com.ds.domain.Servicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author cjbojorquez
 */
@Service
public class ServicioServiceImpl implements ServicioService{

    @Autowired
    private ServicioDao servicioDao;
    
    @Override
    @Transactional(readOnly = true)
    public List<Servicio> listarServicios(Long estado) {
        
        return (List<Servicio>)servicioDao.buscarPorEstado(estado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Servicio> listarPorResidencial(Long idResidencial) {
        
        return (List<Servicio>)servicioDao.buscarPorResidencial(idResidencial);
    }
    
    @Override
    @Transactional
    public void guardar(Servicio servicio) {
        servicioDao.save(servicio);
    }

    @Override
    @Transactional
    public void eliminar(Servicio servicio) {
        servicioDao.delete(servicio);
    }

    
    @Override
    @Transactional(readOnly = true)
    public List<Servicio> listarServicios() {
        return (List<Servicio>)servicioDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Servicio encontrarServicio(Servicio servicio) {
        return servicioDao.findById(servicio.getIdServicio()).orElse(null);
    }
    
}
