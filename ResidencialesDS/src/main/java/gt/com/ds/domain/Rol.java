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
    
    @Column(name="create_time")
    private String fechaCrea;
    
    @Column(name="create_user")
    private Long usuarioCrea;
    
    @Column(name="modify_time")
    private String fechaModifica;
    
    @Column(name="modify_user")
    private Long usuarioModifica;
    
    
    
}
