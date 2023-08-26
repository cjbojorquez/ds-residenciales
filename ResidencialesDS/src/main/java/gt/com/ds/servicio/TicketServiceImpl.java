package gt.com.ds.servicio;

import gt.com.ds.dao.TicketDao;
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
public class TicketServiceImpl implements TicketService{

    @Autowired
    private TicketDao ticketDao;
    
    @Override
    public List<Ticket> ticketsPorTipo(Long idTipo, Long idResidencial) {
        return (List<Ticket>)ticketDao.buscarPorTipo(idTipo,idResidencial);
    }

    @Override
    public List<Ticket> ticketPorEstado(Long idEstado, Long idResidencial) {
        return (List<Ticket>)ticketDao.buscarPorEstado(idEstado,idResidencial);
    }

    @Override
    public List<Ticket> ticketPorUsuario(Long idUsuario) {
        return (List<Ticket>)ticketDao.buscarPorUsuario(idUsuario);
    }

    @Override
    public List<Ticket> listarTicketsAbiertos(Long idResidencial) {
        return (List<Ticket>)ticketDao.buscarActivos(idResidencial);
    }

    @Override
    public void guardar(Ticket ticket) {
        ticketDao.save(ticket);
    }

    @Override
    public void eliminar(Ticket ticket) {
        ticketDao.delete(ticket);
    }

    @Override
    public Ticket encontrarTicket(Ticket ticket) {
        return ticketDao.findById(ticket.getIdTicket()).orElse(null);
    }

    
}
