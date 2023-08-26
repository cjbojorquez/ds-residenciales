package gt.com.ds.util;

import gt.com.ds.domain.Rol;
import gt.com.ds.domain.Usuario;

/**
 *
 * @author cjbojorquez
 */
public class UsuarioRol {

    private Usuario usuario;
    private Rol rol;

    public UsuarioRol(Usuario usuario, Rol rol) {
        this.usuario = usuario;
        this.rol = rol;
    }

    public UsuarioRol(Usuario usuario) {
        this.usuario = usuario;
    }

    public UsuarioRol() {
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    @Override
    public String toString() {
        return "UsuarioRol{" + "usuario=" + usuario + ", rolUsuario=" + rol + '}';
    }
    
    
    
}
