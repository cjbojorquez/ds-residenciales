package gt.com.ds.servicio;

import gt.com.ds.dao.RolUsuarioDao;
import gt.com.ds.domain.RolUsuario;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author cjbojorquez
 */
@Service("userDetailsService")
@Slf4j
public class UserService implements UserDetailsService{
    
    @Autowired
    private RolUsuarioDao rolUsuarioDao;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        RolUsuario rolUsuario = rolUsuarioDao.findByUsername(username);
        
        if(rolUsuario == null){
            throw new UsernameNotFoundException(username);
        }
        
        var roles = new ArrayList<GrantedAuthority>();
        roles.add(new SimpleGrantedAuthority(rolUsuario.getRolUsuario().getRol().getNombre()));
        return new User(rolUsuario.getRolUsuario().getUsuario().getNombreUsuario(),rolUsuario.getRolUsuario().getUsuario().getPassword(),roles);
    }
    
    
}

