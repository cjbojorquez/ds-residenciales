package gt.com.ds.servicio;

import gt.com.ds.domain.Usuario;
import java.util.List;

/**
 *
 * @author cjbojorquez
 */
public interface UsuarioService {
    
    public List<Usuario> listarUsuarios(Long estado);
    public List<Usuario> listarEmpleados(Long estado);
    
    public List<Usuario> listarUsuariosActivos();
    
    public void guardar(Usuario residencial);
    
    public void eliminar(Usuario residencial);
    
    public Usuario encontrarUsuario(Usuario usuario);
    public Usuario encontrarUsuario(Long idUsuario);
    
    public List<Usuario> listarUsuarios();
}
