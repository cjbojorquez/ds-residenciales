package gt.com.ds.servicio;

import gt.com.ds.domain.TipoTicket;
import java.util.List;

/**
 *
 * @author cjbojorquez
 */
public interface TipoTicketService {
    
    public List<TipoTicket> listarTipoTicket();
    
    public void guardar(TipoTicket tipoTicket);
    
    public void eliminar(TipoTicket tipoTicket);
    
    public TipoTicket encontrarTipoTicket(TipoTicket tipoTicket);
}
