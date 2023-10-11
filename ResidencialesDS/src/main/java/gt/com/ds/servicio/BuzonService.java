package gt.com.ds.servicio;

import gt.com.ds.domain.Buzon;
import gt.com.ds.domain.EstadoTicket;
import gt.com.ds.domain.Ticket;
import gt.com.ds.domain.Usuario;
import java.util.List;

/**
 *
 * @author cjbojorquez
 */
public interface BuzonService {
    
    public List<Buzon> buzonPorEstado(Long idEstado,Long idUsuario);
    
    public List<Buzon> buzonPorUsuario(Long idUsuario);
    
    public void guardar(Buzon buzon);
    
    public void eliminar(Buzon buzon);
    
    public Buzon encontrarBuzon(Buzon buzon);
    
    public List<Buzon> buzonNoLeidos(Long idUsuario);
    
    public void cambioEstadoTicket(EstadoTicket actual,Ticket nuevo,Usuario destino,Usuario remite);
}
