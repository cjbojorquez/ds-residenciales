package gt.com.ds.dao;

import gt.com.ds.domain.TipoTicket;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author cjbojorquez
 */
public interface TipoTicketDao extends JpaRepository<TipoTicket,Long>{
    
    
}
