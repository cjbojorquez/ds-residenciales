package gt.com.ds.servicio;

import gt.com.ds.domain.SolicitudServicio;
import java.util.List;

/**
 *
 * @author cjbojorquez
 */
public interface SolicitudServicioService {
    
    public List<SolicitudServicio> listarSolicitudes(Long estado, Long idResidencial);
    public List<SolicitudServicio> listarPorUsuario(Long estado, Long idUsuario);
    
    public List<SolicitudServicio> listarServicios();
    
    public void guardar(SolicitudServicio solicitudServicio);
    
    public void eliminar(SolicitudServicio solicitudServicio);
    
    public SolicitudServicio encontrarServicio(SolicitudServicio solicitudServicio);
}
