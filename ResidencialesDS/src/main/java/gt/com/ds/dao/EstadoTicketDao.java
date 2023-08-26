package gt.com.ds.dao;

import gt.com.ds.domain.EstadoTicket;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author cjbojorquez
 */
public interface EstadoTicketDao extends JpaRepository<EstadoTicket,Long>{
    
}
