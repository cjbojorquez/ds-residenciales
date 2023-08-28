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
@Table(name = "role")
public class Rol implements Serializable {
    
    private static final long serialVerionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idrole")
    private Long idRol;
    
    @NotEmpty
    @Column(name="name")
    private String nombre;
   
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
