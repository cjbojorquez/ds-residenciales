package gt.com.ds.dao;

import gt.com.ds.domain.Ticket;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author cjbojorquez
 */
public interface TicketDao extends JpaRepository<Ticket,Long>{
    
    
    @Query("SELECT t FROM Ticket t WHERE t.estado.idEstado = :idEstado AND t.idResidencial = :idResidencial")
    List<Ticket> buscarPorEstado(@Param("idEstado") Long idEstado,@Param("idResidencial") Long idResidencial);
    
    @Query("SELECT t FROM Ticket t WHERE t.idTipo = :idTipo and t.usuario.idUsuario = :idUsuario")
    List<Ticket> buscarPorUsuario(@Param("idTipo") Long idTipo,@Param("idUsuario") Long idUsuario);
    
    @Query("SELECT t FROM Ticket t WHERE t.idTipo = :idTipo AND t.idResidencial = :idResidencial")
    List<Ticket> buscarPorTipo(@Param("idTipo") Long idTipo,@Param("idResidencial") Long idResidencial);
    
    @Query("SELECT t FROM Ticket t WHERE t.estado.idEstado <> 4 AND t.idResidencial = :idResidencial")
    List<Ticket> buscarActivos(@Param("idResidencial") Long idResidencial);
}
