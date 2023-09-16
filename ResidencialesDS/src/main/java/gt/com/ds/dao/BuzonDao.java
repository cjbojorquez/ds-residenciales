package gt.com.ds.dao;

import gt.com.ds.domain.Buzon;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author cjbojorquez
 */
public interface BuzonDao extends JpaRepository<Buzon,Long>{
    
    
    @Query("SELECT b FROM Buzon b WHERE b.estado = :estado and b.usuario.idUsuario= :idUsuario ORDER BY b.idBuzon DESC")
    List<Buzon> buscarPorEstado(@Param("estado") Long idEstado,@Param("idUsuario") Long idUsuario);
   
    @Query("SELECT b FROM Buzon b WHERE b.usuario.idUsuario = :idUsuario ORDER BY b.idBuzon DESC")
    List<Buzon> buscarPorUsuario(@Param("idUsuario") Long idUsuario);
    
    
}
