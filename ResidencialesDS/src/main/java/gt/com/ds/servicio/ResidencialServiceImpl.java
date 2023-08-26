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
    
    @Override
    @Transactional(readOnly = true)
    public List<Residencial> listarRecidenciales() {
        return (List<Residencial>)residencialDao.findAll();
    }

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

    @Override
    @Transactional(readOnly = true)
    public Residencial encontrarResidencial(Residencial residencial) {
        return residencialDao.findById(residencial.getIdResidential()).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Residencial> listarRecidencialesActivas() {
        return  (List<Residencial>)residencialDao.buscarPorEstado(1L);
    }

    @Override
    public Residencial encontrarPorId(Long id) {
        return residencialDao.buscarPorId(id);
    }
    
}
