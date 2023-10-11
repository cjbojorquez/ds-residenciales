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
public interface SolicitudServicioDao extends JpaRepository<SolicitudServicio, Long> {

    @Query("SELECT s FROM SolicitudServicio s WHERE s.estado = :estado and s.residencial.idResidential =:idResidencial")
    List<SolicitudServicio> buscarPorEstado(@Param("estado") Long estado, @Param("idResidencial") Long idResidencial);

    @Query("SELECT s FROM SolicitudServicio s WHERE s.estado = :estado and s.usuario.idUsuario =:idUsuario")
    List<SolicitudServicio> buscarPorUsuario(@Param("estado") Long estado, @Param("idUsuario") Long idUsuario);

    @Query("SELECT s FROM SolicitudServicio s WHERE s.estado <> 4 and s.residencial.idResidential =:idResidencial")
    List<Object[]> buscarPorResidencial(@Param("idResidencial") Long idResidencial);

    @Query("SELECT COUNT(s.idSolicitud) FROM SolicitudServicio s WHERE s.estado <> 4 and s.residencial.idResidential =:idResidencial")
    Object conteoActivos(@Param("idResidencial") Long idResidencial);

    @Query("SELECT CASE s.estado WHEN 4 THEN 'Cerrado' ELSE 'Abierto' END, COUNT(s.idSolicitud) "
            + "FROM SolicitudServicio s WHERE  s.residencial.idResidential =:idResidencial "
            + "AND MONTH(s.fechaCrea) = MONTH(SYSDATE) AND YEAR(s.fechaCrea) = YEAR(SYSDATE) GROUP BY CASE s.estado WHEN 4 THEN 'Cerrado' ELSE 'Abierto' END")
    List<Object[]> conteoPorEstadoMesActual(@Param("idResidencial") Long idResidencial);

    @Query("SELECT CASE s.estado WHEN 4 THEN 'Cerrado' ELSE 'Abierto' END, COUNT(s.idSolicitud) " +
           "FROM SolicitudServicio s WHERE s.residencial.idResidential = :idResidencial " +
           "AND MONTH(s.fechaCrea) = MONTH(CURRENT_DATE) - 1 AND YEAR(s.fechaCrea) = YEAR(CURRENT_DATE) " +
           "GROUP BY  CASE s.estado WHEN 4 THEN 'Cerrado' ELSE 'Abierto' END")
    List<Object[]> conteoPorEstadoMesAnterior(@Param("idResidencial") Long idResidencial);

//    @Query("SELECT CASE s.estado WHEN 4 THEN 'Cerrado' ELSE 'Abierto' END, COUNT(s.idSolicitud) "
//            + "FROM SolicitudServicio s WHERE s.residencial.idResidential = :idResidencial AND "
//            + "TO_CHAR(s.fechaCrea, 'YYYYMM') = TO_CHAR(ADD_MONTHS(SYSDATE, -1), 'YYYYMM') GROUP BY CASE s.estado WHEN 4 THEN 'Cerrado' ELSE 'Abierto' END")
//    List<Object[]> conteoPorEstadoMesAnterior(@Param("idResidencial") Long idResidencial);
}
