package gt.com.ds.servicio;

import gt.com.ds.dao.RolDao;
import gt.com.ds.domain.Rol;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author cjbojorquez
 */
@Service
public class RolServiceImpl implements RolService{

    @Autowired
    private RolDao rolDao;
    
    @Override
    @Transactional(readOnly = true)
    public List<Rol> listarRoles(Long estado) {
        
        return (List<Rol>)rolDao.buscarPorEstado(estado);
    }

    @Override
    @Transactional
    public void guardar(Rol rol) {
        rolDao.save(rol);
    }

    @Override
    @Transactional
    public void eliminar(Rol rol) {
        rolDao.delete(rol);
    }

    
    @Override
    @Transactional(readOnly = true)
    public List<Rol> listarRoles() {
        return (List<Rol>)rolDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Rol encontrarRol(Rol rol) {
        return rolDao.findById(rol.getIdRol()).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Rol> listarRolesNoAdmin() {
        
        return (List<Rol>)rolDao.rolesNoAdmin();
    }
}
