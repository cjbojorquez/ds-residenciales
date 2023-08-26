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
@Table(name = "message")
public class Notificacion implements Serializable {
    
    private static final long serialVerionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idmessage")
    private Long idNotificacion;
    
    @NotEmpty
    @Column(name="subject")
    private String asunto;
    
    @NotEmpty
    @Column(name="description")
    private String descripcion;
    
    @Column(name="from")
    private String desde;
    
    @Column(name="to")
    private String hasta;
    
    @Column(name="idresidential")
    private Long idResidencial;
    
    @ManyToOne
    @JoinColumn(name = "idstatus")
    private EstadoTicket estado;
    
    @ManyToOne
    @JoinColumn(name = "iduser")
    private Usuario usuario;
    
    @Column(name="attachment")
    private String adjutno;
    
    @Column(name="type")
    private String tipo;
    
    @NotEmpty
    @Column(name="create_time")
    private String fecha_crea;
    
    @Column(name="create_user")
    private Long usuario_crea;
    
    @NotEmpty
    @Column(name="modify_time")
    private String fecha_modifica;
    
    @Column(name="modify_user")
    private Long usuario_modifica;
    
        
}
