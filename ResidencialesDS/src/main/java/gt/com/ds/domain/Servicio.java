package gt.com.ds.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
/**
 * Esta entidad se utiliza para registrar los tipos de servicio que una residencial provee,
 * esta entidad mapea la tabla service
 *
 * @author cjbojorquez
 * 
 */

@Data
@Entity
@Table(name = "service")
public class Servicio implements Serializable {
    
    private static final long serialVerionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idservice")
    private Long idServicio;
    
    @NotEmpty
    @Column(name="name")
    private String nombre;
    
    @Column(name="description")
    private String descripcion;
    
    @ManyToOne
    @JoinColumn(name = "idresidential")
    private Residencial residencial;
    
    @Column(name="status")
    private Long estado;
    
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
