package gt.com.ds.servicio;

import gt.com.ds.domain.Rol;
import gt.com.ds.domain.RolUsuario;
import gt.com.ds.domain.Usuario;
import gt.com.ds.util.UsuarioRol;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author cjbojorquez
 */

@Service
public class UsuarioRolServiceImp implements UsuarioRolService{
    
    @Override
    public List<UsuarioRol> combinarUsuarioConRol(List<Usuario> usuarios, List<RolUsuario> rolesUsuario){
        List<UsuarioRol> usuariosRoles = new ArrayList<>();
        
        for (Usuario usuario : usuarios) {
            UsuarioRol usuarioRol = new UsuarioRol();
            usuarioRol.setUsuario(usuario);

            
            for (RolUsuario rolUsuario : rolesUsuario) {
                // Comprueba si el usuario tiene un rol asignado
                Rol rol=rolUsuario.getRolUsuario().getRol();
                if (rolUsuario.getRolUsuario().getUsuario().getIdUsuario() == usuario.getIdUsuario()) {
                    usuarioRol.setRol(rol); // Establece el código del rol
                    break; // Puedes salir del bucle una vez que se haya encontrado el rol
                }
                
            }
            
            if(usuarioRol.getRol()==null){
                Rol rol = new Rol();
                rol.setIdRol(null);
                rol.setNombre("");
                usuarioRol.setRol(rol);
            }

            usuariosRoles.add(usuarioRol);
        }
        return usuariosRoles;
    }
}
