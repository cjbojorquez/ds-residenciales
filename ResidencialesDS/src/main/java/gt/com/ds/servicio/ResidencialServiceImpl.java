package gt.com.ds.servicio;

import gt.com.ds.dao.ResidencialDao;
import gt.com.ds.domain.Residencial;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author cjbojorquez
 */
@Service
public class ResidencialServiceImpl implements ResidencialService{

    @Autowired
    private ResidencialDao residencialDao;
    
    /**
     * Esta funcion lista todas las residenciales creadas
     * @return 
     */
    @Override
    @Transactional(readOnly = true)
    public List<Residencial> listarRecidenciales() {
        return (List<Residencial>)residencialDao.findAll();
    }

    /**
     * Esta función se utiliza para guardar en base de datos una residencial
     * @param residencial 
     */
    @Override
    @Transactional
    public void guardar(Residencial residencial) {
        residencialDao.save(residencial);
    }

    @Override
    @Transactional
    public void eliminar(Residencial residencial) {
        residencialDao.delete(residencial);
    }

    /**
     * Esta función busca una residencial buscada por el objeto
     * @param residencial
     * @return 
     */
    @Override
    @Transactional(readOnly = true)
    public Residencial encontrarResidencial(Residencial residencial) {
        return residencialDao.findById(residencial.getIdResidential()).orElse(null);
    }

    /**
     * Esta función lista todas las residenciales activas
     * @return 
     */
    @Override
    @Transactional(readOnly = true)
    public List<Residencial> listarRecidencialesActivas() {
        return  (List<Residencial>)residencialDao.buscarPorEstado(1L);
    }

    /**
     * Esta función busca una residencial por el id de la residencial
     * @param id
     * @return 
     */
    @Override
    @Transactional(readOnly = true)
    public Residencial encontrarPorId(Long id) {
        return residencialDao.buscarPorId(id);
    }
    
}
