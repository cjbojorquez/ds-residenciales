package gt.com.ds.dao;

import gt.com.ds.domain.SolicitudServicio;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author cjbojorquez
 */
public interface SolicitudServicioDao extends JpaRepository<SolicitudServicio,Long>{
    
    @Query("SELECT s FROM SolicitudServicio s WHERE s.estado = :estado and s.residencial.idResidential =:idResidencial")
    List<SolicitudServicio> buscarPorEstado(@Param("estado") Long estado, @Param("idResidencial") Long idResidencial);
    
    @Query("SELECT s FROM SolicitudServicio s WHERE s.estado = :estado and s.usuario.idUsuario =:idUsuario")
    List<SolicitudServicio> buscarPorUsuario(@Param("estado") Long estado, @Param("idUsuario") Long idUsuario);
}
