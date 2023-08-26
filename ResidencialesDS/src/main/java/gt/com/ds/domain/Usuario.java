package gt.com.ds.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import java.io.Serializable;
import lombok.Data;
/**
 *
 * @author cjbojorquez
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
    
    @Column(name="create_time")
    private String fechaCrea;
    
    @Column(name="create_user")
    private Long usuarioCrea;
    
    @Column(name="modify_time")
    private String fechaModifica;
    
    @Column(name="modify_user")
    private Long usuarioModifica;
            
}
