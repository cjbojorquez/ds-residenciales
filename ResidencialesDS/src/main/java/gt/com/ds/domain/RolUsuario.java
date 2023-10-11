package gt.com.ds.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Data;


/**
 * Esta entidad almacena los roles asignados a un usuario, se mapea de la tabla role_user
 *
 * @author cjbojorquez
 * 
 */

@Data
@Entity
@Table(name = "role_user")
public class RolUsuario implements Serializable {
    
    private static final long serialVerionUID = 1L;
    
    @EmbeddedId
    private RolUsuarioPK rolUsuario;
    
}
