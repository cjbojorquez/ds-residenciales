package gt.com.ds.dao;

import gt.com.ds.domain.Servicio;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author cjbojorquez
 */
public interface ServicioDao extends JpaRepository<Servicio,Long>{
    
     @Query("SELECT s FROM Servicio s WHERE s.estado = :estado")
    List<Servicio> buscarPorEstado(@Param("estado") Long estado);
}
