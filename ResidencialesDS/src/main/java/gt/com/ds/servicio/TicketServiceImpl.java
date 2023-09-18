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
    @Transactional(readOnly = true)
    public List<Ticket> ticketsPorTipo(Long idTipo, Long idResidencial) {
        return (List<Ticket>)ticketDao.buscarPorTipo(idTipo,idResidencial);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Ticket> ticketPorEstado(Long idEstado, Long idResidencial) {
        return (List<Ticket>)ticketDao.buscarPorEstado(idEstado,idResidencial);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Ticket> ticketPorUsuario(Long idTipo, Long idUsuario) {
        return (List<Ticket>)ticketDao.buscarPorUsuario(idTipo,idUsuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Ticket> listarTicketsAbiertos(Long tipoTicket,Long idResidencial) {
        return (List<Ticket>)ticketDao.buscarPorTipo(tipoTicket,idResidencial);
    }

    @Override
    @Transactional
    public void guardar(Ticket ticket) {
        ticketDao.save(ticket);
    }

    @Override
    @Transactional
    public void eliminar(Ticket ticket) {
        ticketDao.delete(ticket);
    }

    @Override
    @Transactional(readOnly = true)
    public Ticket encontrarTicket(Ticket ticket) {
        return ticketDao.findById(ticket.getIdTicket()).orElse(null);
    }

    @Override
    public Ticket encontrarTicket(Long idTicket) {
        return ticketDao.findById(idTicket).orElse(null);
    }

    
}
