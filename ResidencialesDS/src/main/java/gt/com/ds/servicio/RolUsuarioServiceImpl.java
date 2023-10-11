package gt.com.ds.servicio;

import gt.com.ds.dao.RolUsuarioDao;
import gt.com.ds.domain.RolUsuario;
import gt.com.ds.domain.RolUsuarioPK;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author cjbojorquez
 */
@Service
public class RolUsuarioServiceImpl implements RolUsuarioService {

    @Autowired
    private RolUsuarioDao rolUsuarioDao;

    /**
     * Esta funcion se utilizá para guardar en base de datos el objeto
     * rolUsuario el cual contiene la tupla de rol asingado al usuario
     * @param rolUsuario 
     */
    @Override
    @Transactional
    public void guardar(RolUsuario rolUsuario) {
        
        rolUsuarioDao.save(rolUsuario);
    }

    @Override
    @Transactional
    public void eliminar(RolUsuario rolUsuario) {
        rolUsuarioDao.delete(rolUsuario);
    }

   /**
    * Esta función permite encontrar un rol de usuario buscandolo por el objeto
    * @param rolUsuario
    * @return 
    */
    @Override
    @Transactional(readOnly = true)
    public List<RolUsuario> encontrarRoles(RolUsuario rolUsuario) {
        return rolUsuarioDao.buscarPorUsuario(rolUsuario.getRolUsuario().getUsuario().getIdUsuario());
    }

    /**
     * Esta función permite encontra que rol tiene asignado un usuario buscandolo por 
     * el id del usuario
     * @param idUsuario
     * @return 
     */
    @Override
    @Transactional(readOnly = true)
    public List<RolUsuario> encontrarRoles(Long idUsuario) {
        return rolUsuarioDao.buscarPorUsuario(idUsuario);
    }

    /**
     * Esta función permite listar todas las asignaciones que hay de roles y usuarios
     * @return 
     */
    @Override
    @Transactional(readOnly = true)
    public List<RolUsuario> listarRoles() {
        return rolUsuarioDao.findAll();
    }
    
    

}
