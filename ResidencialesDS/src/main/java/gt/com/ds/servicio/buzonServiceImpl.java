package gt.com.ds.servicio;

import gt.com.ds.dao.BuzonDao;
import gt.com.ds.domain.Buzon;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author cjbojorquez
 */
@Service
public class BuzonServiceImpl implements BuzonService{

    @Autowired
    private BuzonDao buzonDao;
    
   
    @Override
    @Transactional(readOnly = true)
    public List<Buzon> buzonPorEstado(Long idEstado, Long idResidencial) {
        return (List<Buzon>)buzonDao.buscarPorEstado(idEstado,idResidencial);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Buzon> buzonPorUsuario(Long idUsuario) {
        return (List<Buzon>)buzonDao.buscarPorUsuario(idUsuario);
    }

    @Override
    @Transactional
    public void guardar(Buzon buzon) {
        buzonDao.save(buzon);
    }

    @Override
    @Transactional
    public void eliminar(Buzon buzon) {
        buzonDao.delete(buzon);
    }

    @Override
    @Transactional(readOnly = true)
    public Buzon encontrarBuzon(Buzon buzon) {
        return buzonDao.findById(buzon.getIdBuzon()).orElse(null);
    }

    
}
