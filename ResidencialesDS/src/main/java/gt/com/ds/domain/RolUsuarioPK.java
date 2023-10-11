package gt.com.ds.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Data;


/**
 * Esta clase se utiliza para crear una llave compuesta que se utiliza en la entidad RolUsuario
 * mapea las llaves primarias iduser y idrole, de la tabla role_user
 *
 * @author cjbojorquez
 * 
 */
@Data
@Embeddable
public class RolUsuarioPK implements Serializable {
    
    private static final long serialVerionUID = 1L;
    
    @ManyToOne
    @JoinColumn(name = "iduser")
    private Usuario usuario;
    
    @ManyToOne
    @JoinColumn(name = "idrole")
    private Rol rol;
    
    
}
