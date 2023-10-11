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
    
    /**
     * Esta función lista todas las solicitudes de servicio que se han creado
     * @param estado
     * @param idResidencial
     * @return 
     */
    @Override
    @Transactional(readOnly = true)
    public List<SolicitudServicio> listarSolicitudes(Long estado, Long idResidencial) {
        
        return (List<SolicitudServicio>)servicioDao.buscarPorEstado(estado,idResidencial);
    }

    /**
     * Esta función lista las solicitudes de servicios creadas, filtradas por usuario
     * @param estado
     * @param idUsuario
     * @return 
     */
    @Override
    @Transactional(readOnly = true)
    public List<SolicitudServicio> listarPorUsuario(Long estado, Long idUsuario) {
        
        return (List<SolicitudServicio>)servicioDao.buscarPorUsuario(estado,idUsuario);
    }
    
    /**
     * Esta funcio permite guardar en base de datos el objeto solicitudServicio
     * @param solicitudServicio 
     */
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

    /**
     * Esta funcion lista todas las solicitudes de servicio creadas
     * @return 
     */
    @Override
    @Transactional(readOnly = true)
    public List<SolicitudServicio> listarServicios() {
        return (List<SolicitudServicio>)servicioDao.findAll();
    }

    /**
     * ESta funcion se utiliza para encontrar un objeto SolicitudServicio buscandolo por el objeto
     * @param solicitudServicio
     * @return 
     */
    @Override
    @Transactional(readOnly = true)
    public SolicitudServicio encontrarServicio(SolicitudServicio solicitudServicio) {
        return servicioDao.findById(solicitudServicio.getIdSolicitud()).orElse(null);
    }

    /**
     * Esta función lista las solicitudes de servicio filtradas por id de residencial
     * @param idResidencial
     * @return 
     */
    @Override
    public List<Object[]> porResidencial(Long idResidencial) {
        return (List<Object[]>)servicioDao.buscarPorResidencial(idResidencial);
    }

    /**
     * Esta funcion hace un conteo de los tickets abiertos filtrados por residencial
     * @param idResidencial
     * @return 
     */
    @Override
    public Object conteoActivos(Long idResidencial) {
        return (Object)servicioDao.conteoActivos(idResidencial);
    }

    /**
     * Esta funcion hace un conteo de los tickets que estan abiertos y cerrados
     * @param mes este parametro nos indica si se quiere la informcaion del mes actual o
     * la informacion del mes anterior
     * @param idResidencial este es el parametro que nos indica sobre que residencial se realizará la consulta
     * @return retorna un array de objetos con la cantidad de tickes agrupada por abiertos y cerrados
     */
    @Override
    public List<Object[]> conteoPorEstado(int mes, Long idResidencial) {
        if(mes==0){
            return (List<Object[]>)servicioDao.conteoPorEstadoMesActual(idResidencial);
        }else {
            return (List<Object[]>)servicioDao.conteoPorEstadoMesAnterior(idResidencial);
        }
    }
    
}
