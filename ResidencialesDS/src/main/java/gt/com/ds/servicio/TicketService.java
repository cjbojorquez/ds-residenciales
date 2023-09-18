package gt.com.ds.servicio;

import gt.com.ds.domain.Ticket;
import java.util.List;

/**
 *
 * @author cjbojorquez
 */
public interface TicketService {
    
    public List<Ticket> ticketsPorTipo(Long idTipo,Long idResidencial);
    
    public List<Ticket> ticketPorEstado(Long idEstado,Long idResidencial);
    
    public List<Ticket> ticketPorUsuario(Long idTipo,Long idUsuario);
    
    public List<Ticket> listarTicketsAbiertos(Long tipoTicket,Long idResidencial);
    
    public void guardar(Ticket ticket);
    
    public void eliminar(Ticket ticket);
    
    public Ticket encontrarTicket(Ticket ticket);
    
    public Ticket encontrarTicket(Long idTicket);
}
