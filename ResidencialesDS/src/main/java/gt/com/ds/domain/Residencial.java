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
@Table(name = "residential")
public class Residencial implements Serializable {
    
    private static final long serialVerionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idresidential")
    private Long idResidential;
    
    @NotEmpty
    private String name;
    @NotEmpty
    private String address;
    @NotEmpty
    private String phone;
    @NotEmpty
    @Email
    private String email;
    private String nit;
    private String logo;
    private Long status;
    private String create_time;
    private Long create_user;
    private String modify_time;
    private Long modify_user;
    
    
    
}
