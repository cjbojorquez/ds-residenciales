package gt.com.ds.servicio;

import gt.com.ds.domain.RolUsuario;
import java.util.List;

/**
 *
 * @author cjbojorquez
 */
public interface RolUsuarioService {
    
    public void guardar(RolUsuario rolUsuario);
    
    public void eliminar(RolUsuario rolUsuario);
    
    public List<RolUsuario> encontrarRoles(RolUsuario rolUsuario);
    
    public List<RolUsuario> encontrarRoles(Long idUsuario);
    
    public List<RolUsuario> listarRoles();
}
