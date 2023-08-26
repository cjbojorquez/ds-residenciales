package gt.com.ds.servicio;

import gt.com.ds.domain.RolUsuario;
import gt.com.ds.domain.Usuario;
import gt.com.ds.util.UsuarioRol;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cjbojorquez
 */
public interface UsuarioRolService {
    
    public List<UsuarioRol> combinarUsuarioConRol(List<Usuario> usuarios, List<RolUsuario> rolesUsuario);
}
