package gt.com.ds.dao;

import gt.com.ds.domain.Rol;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author cjbojorquez
 */
public interface RolDao extends JpaRepository<Rol,Long>{
    
    
     @Query("SELECT r FROM Rol r WHERE r.estado = :estado")
    List<Rol> buscarPorEstado(@Param("estado") Long estado);
}
