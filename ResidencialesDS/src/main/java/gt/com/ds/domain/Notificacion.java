package gt.com.ds.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
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
    
    @Column(name="start_date")
    private String desde;
    
    @Column(name="final_date")
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
    private String adjunto;
    
    @Column(name="type")
    private String tipo;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="create_time")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date fechaCrea;
    
    @Column(name="create_user")
    private Long usuarioCrea;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="modify_time")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date fechaModifica;
    
    @Column(name="modify_user")
    private Long usuarioModifica;
    
        
}