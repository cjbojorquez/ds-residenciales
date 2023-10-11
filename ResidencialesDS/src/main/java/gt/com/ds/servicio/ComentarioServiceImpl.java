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
    
   
    /**
     * ESta funcion lista todos los comentario filtardos por ticket
     * @param idTicket
     * @return 
     */
    @Override
    @Transactional(readOnly = true)
    public List<Comentario> comentarioPorTicket(Long idTicket) {
        return (List<Comentario>)comentarioDao.buscarPorTicket(idTicket);
    }

    
    /**
     * Esta función permite persistir el objeto a base de datos
     * @param comentario 
     */
    @Override
    @Transactional
    public void guardar(Comentario comentario) {
        comentarioDao.save(comentario);
    }

    /**
     * Esta fucion permite eliminar el objetro de la base de datos, funcion que no se utiliza en esta aplicación
     * @param comentario 
     */
    @Override
    @Transactional
    public void eliminar(Comentario comentario) {
        comentarioDao.delete(comentario);
    }

    /**
     * Lista todos los comentario no leidos, filtrados por usuario
     * @param idUsuario
     * @return 
     */
    @Override
    @Transactional(readOnly = true)
    public List<Comentario> buscaNoLeidos(Long idUsuario) {
        return (List<Comentario>)comentarioDao.buscarNoLeidos(idUsuario);
    }

    /**
     * Lista todos los comentarios no leidos, filtrados por residencial
     * @param idResidencial
     * @return 
     */
    @Override
    @Transactional(readOnly = true)
    public List<Comentario> buscaNoLeidosR(Long idResidencial) {
        return (List<Comentario>)comentarioDao.buscarNoLeidosR(idResidencial);
    }
}
