package gt.com.ds.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import java.io.Serializable;
import lombok.Data;


/**
 *
 * @author cjbojorquez
 */

@Data
@Entity
@Table(name = "ticket_type")
public class TipoTicket implements Serializable {
    
    private static final long serialVerionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idType")
    private Long idTipo;
    
    @NotEmpty
    @Column(name="name")
    private String nombre;
   
    
}
