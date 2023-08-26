package gt.com.ds.servicio;

import gt.com.ds.domain.Servicio;
import java.util.List;

/**
 *
 * @author cjbojorquez
 */
public interface ServicioService {
    
    public List<Servicio> listarServicios(Long estado);
    
    public List<Servicio> listarServicios();
    
    public void guardar(Servicio servicio);
    
    public void eliminar(Servicio servicio);
    
    public Servicio encontrarServicio(Servicio servicio);
}
