package gt.com.ds.servicio;

import gt.com.ds.dao.ContactoDao;
import gt.com.ds.domain.Contacto;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author cjbojorquez
 */
@Service
public class ContactoServiceImpl implements ContactoService{

    @Autowired
    private ContactoDao contactoDao;
   
    /**
     * Esta función lista todos los contactos que tiene asociados una residencial
     * @param idResidencial
     * @return 
     */
    @Override
    @Transactional(readOnly = true)
    public List<Contacto> buscaPorResidencial( Long idResidencial) {
        Long estadoActivo=1L;
        return (List<Contacto>)contactoDao.buscarPorResidencial(estadoActivo,idResidencial);
    }

   
    /**
     * Se utiliza esta funcion para persistir los contactos a la base de datos
     * @param contacto 
     */
    @Override
    @Transactional
    public void guardar(Contacto contacto) {
        contactoDao.save(contacto);
    }

    /**
     * Funcion que elimina los contactos
     * @param contacto 
     */
    @Override
    @Transactional
    public void eliminar(Contacto contacto) {
        contactoDao.delete(contacto);
    }

    /**
     * Esta funcion retorna un contacto buscado por id del contacto
     * @param idContacto
     * @return 
     */
    @Override
    public Contacto encontrarContacto(Long idContacto) {
        return contactoDao.findById(idContacto).orElse(null);
    }

}
