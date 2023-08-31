package gt.com.ds.dao;

import gt.com.ds.domain.RolUsuario;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author cjbojorquez
 */
public interface RolUsuarioDao extends JpaRepository<RolUsuario,Long>{
    
    
    @Query("SELECT r FROM RolUsuario r WHERE r.rolUsuario.usuario.idUsuario = :idUsuario")
    List<RolUsuario> buscarPorUsuario(@Param("idUsuario") Long idUsuario);

    @Query("SELECT r FROM RolUsuario r WHERE r.rolUsuario.usuario.nombreUsuario = :nombreUsuario")
    RolUsuario findByUsername(@Param("nombreUsuario") String nombreUsuario);
    //RolUsuario findByUsername();
}
