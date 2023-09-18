package gt.com.ds.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
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
@Table(name = "service_request")
public class SolicitudServicio implements Serializable {
    
    private static final long serialVerionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idservicerequest")
    private Long idSolicitud;
    
    @NotEmpty
    @Column(name="subject")
    private String asunto;
    
    @Column(name="comment")
    private String comentario;
    
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column(name="start_date")
    private Date fecha;
    
    @ManyToOne
    @JoinColumn(name = "iduser")
    private Usuario usuario;
    
    @ManyToOne
    @JoinColumn(name = "idemployee")
    private Usuario empleado;
    
    @ManyToOne
    @JoinColumn(name = "idresidential")
    private Residencial residencial;
    
    @Column(name="status")
    private Long estado;
    
    @ManyToOne
    @JoinColumn(name = "idservice")
    private Servicio servicio;
    
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column(name="create_time")
    private Date fechaCrea;
    
    @Column(name="create_user")
    private Long usuarioCrea;
    
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column(name="modify_time")
    private Date fechaModifica;
    
    @Column(name="modify_user")
    private Long usuarioModifica;
            
}
