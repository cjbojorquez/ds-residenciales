package gt.com.ds.servicio;

import gt.com.ds.domain.Comentario;
import java.util.List;

/**
 *
 * @author cjbojorquez
 */
public interface ComentarioService {
    
    public List<Comentario> comentarioPorTicket(Long idTicket);
    
    public void guardar(Comentario comentario);
    
    public void eliminar(Comentario comentario);
    
}
