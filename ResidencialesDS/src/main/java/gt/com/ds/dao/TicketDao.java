package gt.com.ds.dao;

import gt.com.ds.domain.Conteo;
import gt.com.ds.domain.Ticket;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author cjbojorquez
 */
public interface TicketDao extends JpaRepository<Ticket, Long> {

    @Query("SELECT t FROM Ticket t WHERE t.estado.idEstado = :idEstado AND t.idResidencial = :idResidencial")
    List<Ticket> buscarPorEstado(@Param("idEstado") Long idEstado, @Param("idResidencial") Long idResidencial);

    @Query("SELECT t FROM Ticket t WHERE t.idTipo = :idTipo and t.usuario.idUsuario = :idUsuario")
    List<Ticket> buscarPorUsuario(@Param("idTipo") Long idTipo, @Param("idUsuario") Long idUsuario);

    @Query("SELECT t FROM Ticket t WHERE t.idTipo = :idTipo AND t.idResidencial = :idResidencial")
    List<Ticket> buscarPorTipo(@Param("idTipo") Long idTipo, @Param("idResidencial") Long idResidencial);

    @Query("SELECT t FROM Ticket t WHERE t.estado.idEstado <> 4 AND t.idResidencial = :idResidencial")
    List<Ticket> buscarActivos(@Param("idResidencial") Long idResidencial);

    @Query("SELECT CASE t.idTipo WHEN 1 THEN 'Gestion' ELSE 'Anomalia' END,COUNT(1) FROM Ticket t WHERE t.estado.idEstado <> 4 AND t.idResidencial = :idResidencial GROUP BY t.idTipo")
    List<Object[]> countActivos(@Param("idResidencial") Long idResidencial);

    @Query("SELECT CASE t.estado.idEstado WHEN 4 THEN 'Cerrado' ELSE 'Abierto' END es,COUNT(1) "
            + "FROM Ticket t WHERE t.estado.idEstado <> 4 AND t.idResidencial = :idResidencial "
            + "AND MONTH(t.fecha) = MONTH(SYSDATE) AND YEAR(t.fecha) = YEAR(SYSDATE) GROUP BY CASE t.estado.idEstado WHEN 4 THEN 'Cerrado' ELSE 'Abierto' END"
            + " UNION SELECT CASE s.estado WHEN 4 THEN 'Cerrado' ELSE 'Abierto' END es, COUNT(s.idSolicitud) "
            + "FROM SolicitudServicio s WHERE  s.residencial.idResidential =:idResidencial "
            + "AND MONTH(s.fechaCrea) = MONTH(SYSDATE) AND YEAR(s.fechaCrea) = YEAR(SYSDATE) GROUP BY CASE s.estado WHEN 4 THEN 'Cerrado' ELSE 'Abierto' END")
            
    List<Object[]> conteoPorEstadoMesActual(@Param("idResidencial") Long idResidencial);

    @Query("SELECT CASE WHEN t.estado.idEstado = 4 THEN 'Cerrado' ELSE 'Abierto' END, COUNT(1) "
            + " FROM Ticket t WHERE t.idResidencial = :idResidencial "
            + " AND MONTH(t.fecha) = MONTH(CURRENT_DATE) - 1 AND YEAR(t.fecha) = YEAR(CURRENT_DATE) " 
            + " GROUP BY  CASE WHEN t.estado.idEstado = 4 THEN 'Cerrado' ELSE 'Abierto' END")
    List<Object[]> conteoPorEstadoMesAnterior(@Param("idResidencial") Long idResidencial);
    /*@Query("SELECT tmp.es, COUNT(tmp.es) FROM (SELECT CASE WHEN t.estado.idEstado = 4 THEN 'Cerrado' ELSE 'Abierto' END AS es "
        + " FROM Ticket t WHERE t.idResidencial = :idResidencial "
        + " AND MONTH(t.fecha) = MONTH(CURRENT_DATE) - 1 AND YEAR(t.fecha) = YEAR(CURRENT_DATE) " +
        " UNION " +
        "SELECT CASE s.estado WHEN 4 THEN 'Cerrado' ELSE 'Abierto' END AS es" +
        " FROM SolicitudServicio s WHERE s.residencial.idResidential = :idResidencial " +
        " AND MONTH(s.fechaCrea) = MONTH(CURRENT_DATE) - 1 AND YEAR(s.fechaCrea) = YEAR(CURRENT_DATE)) tmp " +
        "GROUP BY tmp.es")
List<Object[]> conteoPorEstadoMesAnterior(@Param("idResidencial") Long idResidencial);*/

    
//    @Query("SELECT tmp.es,COUNT(tmp.es) FROM (SELECT CASE WHEN t.estado.idEstado = 4 THEN 'Cerrado' ELSE 'Abierto' END  AS es "
//            + " FROM Ticket t WHERE  t.idResidencial = :idResidencial "
//            + " AND MONTH(t.fecha) = MONTH(CURRENT_DATE) - 1 AND YEAR(t.fecha) = YEAR(CURRENT_DATE) " +
//           " "
//            + " UNION "
//            + "SELECT CASE s.estado WHEN 4 THEN 'Cerrado' ELSE 'Abierto' END ASs es" +
//           " FROM SolicitudServicio s WHERE s.residencial.idResidential = :idResidencial " +
//           " AND MONTH(s.fechaCrea) = MONTH(CURRENT_DATE) - 1 AND YEAR(s.fechaCrea) = YEAR(CURRENT_DATE)) tmp " +
//           " GROUP BY tmp.es")
//    List<Object[]> conteoPorEstadoMesAnterior(@Param("idResidencial") Long idResidencial);
    
    
    
//    @Query("SELECT CASE t.estado.idEstado WHEN 4 THEN 'Cerrado' ELSE 'Abierto' END,COUNT(1) "
//            + "FROM Ticket t WHERE t.estado.idEstado <> 4 AND t.idResidencial = :idResidencial "
//            + "AND TO_CHAR(t.fecha, 'YYYYMM') = TO_CHAR(ADD_MONTHS(SYSDATE, -1), 'YYYYMM') GROUP BY CASE t.estado.idEstado WHEN 4 THEN 'Cerrado' ELSE 'Abierto' END")
//    List<Object[]> conteoPorEstadoMesAnterior(@Param("idResidencial") Long idResidencial);
}
