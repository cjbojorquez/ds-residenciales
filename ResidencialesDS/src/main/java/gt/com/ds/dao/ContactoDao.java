package gt.com.ds.dao;

import gt.com.ds.domain.Contacto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author cjbojorquez
 */
public interface ContactoDao extends JpaRepository<Contacto,Long>{
    
    
    @Query("SELECT c FROM Contacto c WHERE c.idEstado = :estado and c.residencial.idResidential= :idResidencial")
    List<Contacto> buscarPorResidencial(@Param("estado") Long idEstado,@Param("idResidencial") Long idResidencial);
}
