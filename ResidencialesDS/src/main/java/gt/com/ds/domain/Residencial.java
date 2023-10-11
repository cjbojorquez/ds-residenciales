package gt.com.ds.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;


/**
 * Esta entidad mapea la tabla residential, la cual contiene las residenciales creadas
 *
 * @author cjbojorquez
 * 
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
    
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date create_time;
    
    private Long create_user;
    
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date modify_time;
    
    private Long modify_user;
    
    
    
}
