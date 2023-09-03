package gt.com.ds.dao;

import gt.com.ds.domain.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author cjbojorquez
 */
public interface UsuarioDao extends JpaRepository<Usuario,Long>{
    
    @Query("SELECT u FROM Usuario u WHERE u.esEmpleado = :empleado and u.estado=:estado")
    List<Usuario> buscaUsuariosTipo(@Param("empleado") Long empleado,@Param("estado") Long estado);
    
    @Query("SELECT r FROM Usuario r WHERE r.estado = :status")
    List<Usuario> buscarPorEstado(@Param("status") Long status);
    
    @Query("SELECT r FROM Usuario r WHERE r.nombreUsuario <> '' AND r.nombreUsuario IS NOT NULL")
    List<Usuario> listarUsuarios();
    
    //@Query("SELECT u FROM Usuario u WHERE u.nombreUsuario = :nombreUsuario and u.residencial.idResidential=:idResidencial")
    //Usuario buscarUsuario(@Param("nombreUsuario") String nombreUsuario,@Param("idResidencial") Long idResidencial);
    @Query("SELECT u FROM Usuario u WHERE u.nombreUsuario = :nombreUsuario")
    Usuario buscarUsuario(@Param("nombreUsuario") String nombreUsuario);
}
