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
@Table(name = "ticket")
public class Ticket implements Serializable {
    
    private static final long serialVerionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idticket")
    private Long idTicket;
    
    @NotEmpty
    @Column(name="subject")
    private String asunto;
    
    @NotEmpty
    @Column(name="description")
    private String descripcion;
    
    @NotEmpty
    @Column(name="creation_date")
    private String fecha;
    
    @ManyToOne
    @JoinColumn(name = "iduser")
    private Usuario usuario;
    
    @Column(name="idticket_type")
    private Long idTipo;
    
    @ManyToOne
    @JoinColumn(name = "idstatus")
    private EstadoTicket estado;
    
    @Column(name="idresidential")
    private Long idResidencial;
    
        
}
