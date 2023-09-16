package gt.com.ds.dao;

import gt.com.ds.domain.Comentario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author cjbojorquez
 */
public interface ComentarioDao extends JpaRepository<Comentario,Long>{
    
    @Query("SELECT c FROM Comentario c WHERE c.ticket.idTicket= :idTicket ORDER BY c.fecha")
    List<Comentario> buscarPorTicket(@Param("idTicket") Long idTicket);
       
}
