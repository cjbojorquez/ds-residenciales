package gt.com.ds.dao;

import gt.com.ds.domain.Notificacion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author cjbojorquez
 */
public interface NotificacionDao extends JpaRepository<Notificacion,Long>{
    
    
    @Query("SELECT t FROM Notificacion t WHERE t.tipo = :tipo AND t.estado.idEstado = :idEstado AND t.idResidencial = :idResidencial")
    List<Notificacion> buscarPorEstado(@Param("tipo") String tipo,@Param("idEstado") Long idEstado,@Param("idResidencial") Long idResidencial);
    
    @Query("SELECT t FROM Notificacion t WHERE t.tipo = :tipo AND t.usuario.idUsuario = :idUsuario")
    List<Notificacion> buscarPorUsuario(@Param("tipo") String tipo,@Param("idUsuario") Long idUsuario);
    
    @Query("SELECT t FROM Notificacion t WHERE t.tipo = :tipo AND t.idResidencial = :idResidencial")
    List<Notificacion> buscarPorTipo(@Param("tipo") String tipo,@Param("idResidencial") Long idResidencial);
    
    @Query("SELECT t FROM Notificacion t WHERE t.tipo = :tipo AND t.estado.idEstado in(1,2) AND t.idResidencial = :idResidencial")
    List<Notificacion> buscarActivos(@Param("tipo") String tipo,@Param("idResidencial") Long idResidencial);
}
