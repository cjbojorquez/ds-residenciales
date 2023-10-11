package gt.com.ds.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
/**
 * Esta entidad mapea la tabla contact, que registra los contactos asociados a una residencial
 *
 * @author cjbojorquez
 */

@Data
@Entity
@Table(name = "contact")
public class Contacto implements Serializable {
    
    private static final long serialVerionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idcontact")
    private Long idContacto;
    
    @Column(name="name")
    private String nombre;
    
    @Column(name="description")
    private String descripcion;
    
    @Column(name="phone")
    private String telefono;
    
    @Column(name="status")
    private Long idEstado;
    
    @ManyToOne
    @JoinColumn(name = "idresidential")
    private Residencial residencial;
    
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
