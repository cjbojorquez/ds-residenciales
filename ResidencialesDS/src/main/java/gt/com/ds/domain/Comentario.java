package gt.com.ds.domain;

import jakarta.persistence.*;
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
@Table(name = "comment")
public class Comentario implements Serializable {
    
    private static final long serialVerionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idcomment")
    private Long idComentario;
    
    @ManyToOne
    @JoinColumn(name = "idticket")
    private Ticket ticket;
    
    @ManyToOne
    @JoinColumn(name = "iduser")
    private Usuario usuario;
    
    @Column(name="status")
    private Long idEstado;
    
    @Column(name="attachment")
    private String adjunto;
    
    @Column(name="comment")
    private String comentario;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date fecha;
    
    
}
