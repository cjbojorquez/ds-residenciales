package gt.com.ds.servicio;

import gt.com.ds.domain.Rol;
import java.util.List;

/**
 *
 * @author cjbojorquez
 */
public interface RolService {
    
    public List<Rol> listarRoles(Long estado);
    
    public List<Rol> listarRoles();
    
    public void guardar(Rol rol);
    
    public void eliminar(Rol rol);
    
    public Rol encontrarRol(Rol rol);
}
