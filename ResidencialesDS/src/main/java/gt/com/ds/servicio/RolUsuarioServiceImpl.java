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

   
    @Override
    public List<RolUsuario> encontrarRoles(RolUsuario rolUsuario) {
        return rolUsuarioDao.buscarPorUsuario(rolUsuario.getRolUsuario().getUsuario().getIdUsuario());
    }

    @Override
    public List<RolUsuario> encontrarRoles(Long idUsuario) {
        return rolUsuarioDao.buscarPorUsuario(idUsuario);
    }

    @Override
    public List<RolUsuario> listarRoles() {
        return rolUsuarioDao.findAll();
    }
    
    

}
