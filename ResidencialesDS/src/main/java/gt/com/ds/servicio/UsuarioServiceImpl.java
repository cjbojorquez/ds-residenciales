package gt.com.ds.servicio;

import gt.com.ds.dao.UsuarioDao;
import gt.com.ds.domain.Usuario;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author cjbojorquez
 */
@Service
public class UsuarioServiceImpl implements UsuarioService{

    @Autowired
    private UsuarioDao usuarioDao;
    
    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarUsuarios(Long estado) {
        return (List<Usuario>)usuarioDao.buscaUsuariosTipo(0L,estado);
    }

    @Override
    @Transactional
    public Long guardar(Usuario residencial) {
        return usuarioDao.save(residencial).getIdUsuario();
    }

    @Override
    @Transactional
    public void eliminar(Usuario residencial) {
        usuarioDao.delete(residencial);
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario encontrarUsuario(Usuario usuario) {
        return usuarioDao.findById(usuario.getIdUsuario()).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarUsuariosActivos() {
        return  (List<Usuario>)usuarioDao.buscarPorEstado(1L);
    }

    @Override
    public List<Usuario> listarEmpleados(Long estado) {
        return (List<Usuario>)usuarioDao.buscaUsuariosTipo(1L,estado);
    }

    @Override
    public Usuario encontrarUsuario(Long idUsuario) {
        return usuarioDao.findById(idUsuario).get();
    }

    @Override
    public List<Usuario> listarUsuarios() {
        return usuarioDao.listarUsuarios();
    }

    @Override
    public Usuario encontrarUsuario(String nombreUsuario) {
        
        //return usuarioDao.buscarUsuario(nombreUsuario, idResidencial);
        return usuarioDao.buscarUsuario(nombreUsuario);
    }

    @Override
    public List<Usuario> listarEmpleadosResidencial(Long estado, Long idResidencial) {
        return (List<Usuario>)usuarioDao.buscaUsuariosResidencial(1L,estado,idResidencial);
    }

    @Override
    public List<Usuario> listarUsuariosResidencial(Long estado, Long idResidencial) {
        return (List<Usuario>)usuarioDao.buscaUsuariosResidencial(0L,estado,idResidencial);
    }
    
}
