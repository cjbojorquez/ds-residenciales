package gt.com.ds.servicio;

import gt.com.ds.dao.TipoTicketDao;
import gt.com.ds.domain.TipoTicket;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author cjbojorquez
 */
@Service
public class TipoTicketServiceImpl implements TipoTicketService{

    @Autowired
    private TipoTicketDao tipoTicketDao;
    

    @Override
    @Transactional
    public void guardar(TipoTicket tipoTicket) {
        tipoTicketDao.save(tipoTicket);
    }

    @Override
    @Transactional
    public void eliminar(TipoTicket tipoTicket) {
        tipoTicketDao.delete(tipoTicket);
    }

    
    @Override
    @Transactional(readOnly = true)
    public List<TipoTicket> listarTipoTicket() {
        return (List<TipoTicket>)tipoTicketDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public TipoTicket encontrarTipoTicket(TipoTicket tipoTicket) {
        return tipoTicketDao.findById(tipoTicket.getIdTipo()).orElse(null);
    }

    
}
