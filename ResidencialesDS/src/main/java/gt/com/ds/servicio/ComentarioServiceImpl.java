package gt.com.ds.servicio;

import gt.com.ds.dao.ComentarioDao;
import gt.com.ds.domain.Comentario;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author cjbojorquez
 */
@Service
public class ComentarioServiceImpl implements ComentarioService{

    @Autowired
    private ComentarioDao comentarioDao;
    
   
    @Override
    @Transactional(readOnly = true)
    public List<Comentario> comentarioPorTicket(Long idTicket) {
        return (List<Comentario>)comentarioDao.buscarPorTicket(idTicket);
    }

    

    @Override
    @Transactional
    public void guardar(Comentario comentario) {
        comentarioDao.save(comentario);
    }

    @Override
    @Transactional
    public void eliminar(Comentario comentario) {
        comentarioDao.delete(comentario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comentario> buscaNoLeidos(Long idUsuario) {
        return (List<Comentario>)comentarioDao.buscarNoLeidos(idUsuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comentario> buscaNoLeidosR(Long idResidencial) {
        return (List<Comentario>)comentarioDao.buscarNoLeidosR(idResidencial);
    }
}
