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
    
    /**
     * Lista a todos los usuarios por estado
     * @param estado
     * @return 
     */
    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarUsuarios(Long estado) {
        return (List<Usuario>)usuarioDao.buscaUsuariosTipo(0L,estado);
    }

    /**
     * Persiste el objeto usuario a base de datos
     * @param usuario
     * @return 
     */
    @Override
    @Transactional
    public Long guardar(Usuario usuario) {
        return usuarioDao.save(usuario).getIdUsuario();
    }

    
    @Override
    @Transactional
    public void eliminar(Usuario residencial) {
        usuarioDao.delete(residencial);
    }

    /**
     * busca un usuario por medio del objeto usuario
     * @param usuario
     * @return 
     */
    @Override
    @Transactional(readOnly = true)
    public Usuario encontrarUsuario(Usuario usuario) {
        return usuarioDao.findById(usuario.getIdUsuario()).orElse(null);
    }

    /**
     * Lista a todos los usuarios activos
     * @return 
     */
    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarUsuariosActivos() {
        return  (List<Usuario>)usuarioDao.buscarPorEstado(1L);
    }

    /**
     * Lista a todos los empleados activos
     * @param estado
     * @return 
     */
    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarEmpleados(Long estado) {
        return (List<Usuario>)usuarioDao.buscaUsuariosTipo(1L,estado);
    }

    /**
     * Busca a un usuario por medio del idUsuario
     * @param idUsuario
     * @return 
     */
    @Override
    @Transactional(readOnly = true)
    public Usuario encontrarUsuario(Long idUsuario) {
        return usuarioDao.findById(idUsuario).get();
    }

    /**
     * Lista a todos los usuarios
     * @return 
     */
    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarUsuarios() {
        return usuarioDao.listarUsuarios();
    }

    /**
     * busca un usuario por medio del user name
     * @param nombreUsuario
     * @return 
     */
    @Override
    @Transactional(readOnly = true)
    public Usuario encontrarUsuario(String nombreUsuario) {
        
        //return usuarioDao.buscarUsuario(nombreUsuario, idResidencial);
        return usuarioDao.buscarUsuario(nombreUsuario);
    }

    /**
     * Lista a todos los empleados de una residencial determinada y por estado
     * @param estado
     * @param idResidencial
     * @return 
     */
    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarEmpleadosResidencial(Long estado, Long idResidencial) {
        return (List<Usuario>)usuarioDao.buscaUsuariosResidencial(1L,estado,idResidencial);
    }

    /**
     * Lista a todos los usuario por residencial y por estado
     * @param estado
     * @param idResidencial
     * @return 
     */
    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarUsuariosResidencial(Long estado, Long idResidencial) {
        return (List<Usuario>)usuarioDao.buscaUsuariosResidencial(0L,estado,idResidencial);
    }

    /**
     * Lista usuarios y empleados por estado y por residencial
     * @param estado
     * @param idResidencial
     * @return 
     */
    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarUsuariosxResidencial(Long estado,Long idResidencial) {
        return (List<Usuario>)usuarioDao.listarUsuariosResidencial(estado,idResidencial);
    }
    
}
