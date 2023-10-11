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
    
    /**
     * Esta función lista todos los servicios filtrados por estado
     * @param estado
     * @return 
     */
    @Override
    @Transactional(readOnly = true)
    public List<Servicio> listarServicios(Long estado) {
        
        return (List<Servicio>)servicioDao.buscarPorEstado(estado);
    }

    /**
     * Esta función lista los servicios filtrados por id de la residencial
     * @param idResidencial
     * @return 
     */
    @Override
    @Transactional(readOnly = true)
    public List<Servicio> listarPorResidencial(Long idResidencial) {
        
        return (List<Servicio>)servicioDao.buscarPorResidencial(idResidencial);
    }
    
    /**
     * Por medio de esta función se guardan los serivicios en la base de datos
     * @param servicio 
     */
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

    /**
     * Esta fincion lista todos los servicios creados
     * @return 
     */
    @Override
    @Transactional(readOnly = true)
    public List<Servicio> listarServicios() {
        return (List<Servicio>)servicioDao.findAll();
    }

    /**
     * Esta funcio encuentra un servicio buscandolo por el objeto
     * @param servicio
     * @return 
     */
    @Override
    @Transactional(readOnly = true)
    public Servicio encontrarServicio(Servicio servicio) {
        return servicioDao.findById(servicio.getIdServicio()).orElse(null);
    }
    
}
