package gt.com.ds.servicio;

import gt.com.ds.domain.Contacto;
import java.util.List;

/**
 *
 * @author cjbojorquez
 */
public interface ContactoService {
    
    public List<Contacto> buscaPorResidencial(Long idResidencial);
    
    public void guardar(Contacto contacto);
    
    public void eliminar(Contacto contacto);
    
    public Contacto encontrarContacto(Long idContacto);
    
}
