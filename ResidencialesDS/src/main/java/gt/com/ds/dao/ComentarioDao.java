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
    
    @Query("SELECT c FROM Comentario c WHERE c.id IN " +
        "(SELECT MAX(c2.id) FROM Comentario c2 " +
        " WHERE c2.idEstado = 1 AND c2.usuario.idUsuario != :idUsuario AND "
            + "c2.ticket.usuario.idUsuario=:idUsuario" +
        " GROUP BY c2.ticket) ")
    List<Comentario> buscarNoLeidos(@Param("idUsuario") Long idUsuario);
    
    @Query("SELECT c FROM Comentario c WHERE c.id IN " +
        "(SELECT MAX(c2.id) FROM Comentario c2 " +
        " WHERE c2.idEstado = 1 AND c2.usuario.residencial.idResidential = :idResidencial " +
        " GROUP BY c2.ticket) ")
    List<Comentario> buscarNoLeidosR(@Param("idResidencial") Long idResidencial);
       
}
