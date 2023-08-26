package gt.com.ds.servicio;

import gt.com.ds.domain.EstadoTicket;
import java.util.List;

/**
 *
 * @author cjbojorquez
 */
public interface EstadoTicketService {
    
    public List<EstadoTicket> listarEstadoTicket();
    
    public void guardar(EstadoTicket estadoTicket);
    
    public void eliminar(EstadoTicket estadoTicket);
    
    public EstadoTicket encontrarEstado(EstadoTicket estadoTicket);
    
    public EstadoTicket encontrarEstado(Long idEstado);
}
