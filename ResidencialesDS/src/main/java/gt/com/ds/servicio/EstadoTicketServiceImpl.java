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
    
    /**
     * Esta función permite persistir el objeto a base de datos
     * @param estadoTicket 
     */
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

    /**
     * Esta funcion lista todos los tipos de estados para los tickets
     * @return 
     */
    @Override
    @Transactional(readOnly = true)
    public List<EstadoTicket> listarEstadoTicket() {
        return (List<EstadoTicket>)estadoTicketDao.findAll();
    }

    /**
     * Esta función retorna un estado de ticket filtrado por el objeto
     * @param estadoTicket
     * @return 
     */
    @Override
    @Transactional(readOnly = true)
    public EstadoTicket encontrarEstado(EstadoTicket estadoTicket) {
        return estadoTicketDao.findById(estadoTicket.getIdEstado()).orElse(null);
    }

    /**
     * Esta función retorna un tipo de estado el cual es buscado por el id del estado
     * @param idEstado
     * @return 
     */
    @Override
    @Transactional(readOnly = true)
    public EstadoTicket encontrarEstado(Long idEstado) {
        return estadoTicketDao.findById(idEstado).orElse(null);
    }

    
}
