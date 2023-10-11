package gt.com.ds.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
/**
 * Esta entidad mapea la tabla user, que lleva el control de usuarios y empleados
 *
 * @author cjbojorquez
 * 
 */

@Data
@Entity
@Table(name = "user")
public class Usuario implements Serializable {
    
    private static final long serialVerionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="iduser")
    private Long idUsuario;
    
    @NotEmpty
    @Column(name="name")
    private String nombre;
    
    @Column(name="username")
    private String nombreUsuario;
    
    @Column(name="password")
    private String password;
    
    @NotEmpty
    @Email
    private String email;
    
    @NotEmpty
    @Column(name="phone")
    private String telefono;
    
    @NotEmpty
    @Column(name="code")
    private String codigo;
    
    @Column(name="address")
    private String direccion;
    
    @Column(name="position")
    private String cargo;
    
    @Column(name="photo")
    private String foto;
    
    @Column(name="employee")
    private Long esEmpleado;
    
    @ManyToOne
    @JoinColumn(name = "idresidential")
    private Residencial residencial;
    
    @Column(name="status")
    private Long estado;
    
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
