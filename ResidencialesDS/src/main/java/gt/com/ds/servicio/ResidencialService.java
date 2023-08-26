package gt.com.ds.servicio;

import gt.com.ds.domain.Residencial;
import java.util.List;

/**
 *
 * @author cjbojorquez
 */
public interface ResidencialService {
    
    public List<Residencial> listarRecidenciales();
    
    public List<Residencial> listarRecidencialesActivas();
    
    public void guardar(Residencial residencial);
    
    public void eliminar(Residencial residencial);
    
    public Residencial encontrarResidencial(Residencial residencial);
    
    public Residencial encontrarPorId(Long id);
}
