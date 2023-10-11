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
    
    /**
     * Lista todos los roles filtrados por su estado
     * @param estado
     * @return 
     */
    @Override
    @Transactional(readOnly = true)
    public List<Rol> listarRoles(Long estado) {
        
        return (List<Rol>)rolDao.buscarPorEstado(estado);
    }

    /**
     * Esta funcion se utiliza para persistir el objeto rol a la base de datos
     * @param rol 
     */
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

    /**
     * Esta función lista todos los roles creados
     * @return 
     */
    @Override
    @Transactional(readOnly = true)
    public List<Rol> listarRoles() {
        return (List<Rol>)rolDao.findAll();
    }

    /**
     * Esta función se utiliza para encontrar un rol buscandolo por el objeto
     * @param rol
     * @return 
     */
    @Override
    @Transactional(readOnly = true)
    public Rol encontrarRol(Rol rol) {
        return rolDao.findById(rol.getIdRol()).orElse(null);
    }

    /**
     * Esta funcion lista todos los roles que no son administrador
     * @return 
     */
    @Override
    @Transactional(readOnly = true)
    public List<Rol> listarRolesNoAdmin() {
        
        return (List<Rol>)rolDao.rolesNoAdmin();
    }
}
