package gt.com.ds.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Data;


/**
 *
 * @author cjbojorquez
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
