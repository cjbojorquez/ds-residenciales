package gt.com.ds.servicio;

import gt.com.ds.dao.EstadoTicketDao;
import gt.com.ds.domain.EstadoTicket;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author cjbojorquez
 */
@Service
public class EstadoTicketServiceImpl implements EstadoTicketService{

    @Autowired
    private EstadoTicketDao estadoTicketDao;
    

    @Override
    @Transactional
    public void guardar(EstadoTicket estadoTicket) {
        estadoTicketDao.save(estadoTicket);
    }

    @Override
    @Transactional
    public void eliminar(EstadoTicket estadoTicket) {
        estadoTicketDao.delete(estadoTicket);
    }

    
    @Override
    public List<EstadoTicket> listarEstadoTicket() {
        return (List<EstadoTicket>)estadoTicketDao.findAll();
    }

    @Override
    public EstadoTicket encontrarEstado(EstadoTicket estadoTicket) {
        return estadoTicketDao.findById(estadoTicket.getIdEstado()).orElse(null);
    }

    @Override
    public EstadoTicket encontrarEstado(Long idEstado) {
        return estadoTicketDao.findById(idEstado).orElse(null);
    }

    
}
