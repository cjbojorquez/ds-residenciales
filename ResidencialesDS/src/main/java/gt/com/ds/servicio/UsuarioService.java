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
    public List<Usuario> listarEmpleadosResidencial(Long estado, Long idResidencial);
    public List<Usuario> listarUsuariosResidencial(Long estado, Long idResidencial);
    public List<Usuario> listarUsuariosActivos();
    
    public Long guardar(Usuario residencial);
    
    public void eliminar(Usuario residencial);
    
    public Usuario encontrarUsuario(Usuario usuario);
    public Usuario encontrarUsuario(Long idUsuario);
    public Usuario encontrarUsuario(String nombreUsuario);
    
    public List<Usuario> listarUsuarios();
}
