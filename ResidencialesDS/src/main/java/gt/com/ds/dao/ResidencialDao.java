package gt.com.ds.dao;

import gt.com.ds.domain.Residencial;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author cjbojorquez
 */
public interface ResidencialDao extends JpaRepository<Residencial,Long>{
    
    @Query("SELECT r FROM Residencial r WHERE r.status = :status")
    List<Residencial> buscarPorEstado(@Param("status") Long status);
    
    @Query("SELECT r FROM Residencial r WHERE r.idResidential = :id")
    Residencial buscarPorId(@Param("id") Long id);
}
