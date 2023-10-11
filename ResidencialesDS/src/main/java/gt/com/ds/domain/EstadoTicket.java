package gt.com.ds.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import java.io.Serializable;
import lombok.Data;


/**
 * Esta entidad mapea la tabla ticket_status, la cual contiene los tipos de tickets que se
 * manejaran, Gestiones y Anomalias
 *
 * @author cjbojorquez
 * 
 */

@Data
@Entity
@Table(name = "ticket_status")
public class EstadoTicket implements Serializable {
    
    private static final long serialVerionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idstatus")
    private Long idEstado;
    
    @NotEmpty
    @Column(name="description")
    private String nombre;
   
    
}
