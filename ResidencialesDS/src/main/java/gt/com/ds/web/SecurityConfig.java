package gt.com.ds.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

/**
 * En esta clase esta la configuracion de la seguridad de la aplicación
 *
 * @author cjbojorquez
 * 
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {// extends WebSecurityConfigurerAdapter{

    //@Autowired
    private UserDetailsService userDetailsService;

    /**
     * esta funcion permite encriptar la contraseña de los usuarios
     * @return retorna la contraseña encriptada
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //@Autowired
    
    public void configurerGlobal(AuthenticationManagerBuilder build) throws Exception {
        build.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    /**
     * en esta función se configura cual sera la pagina a la que se debe redireccionar para 
     * la autenticacion.
     * @return 
     */
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new LoginUrlAuthenticationEntryPoint("/login"); 
    }

    /**
     * En esta funcion configuramos los accesos por rol, cada pagina esta mapeada con un rol para que 
     * este tenga acceso a dicha pagina, para el optimo funcionamiento solo se debe mapear una vez cada pagina, 
     * si hay varios roles que deben tener acceso a la pagina, se configuran todos los que deben tener
     * acceso, pero no se debe colocar mas de dos veces, ya que la segunda ya no se toma en cuenta.
     * 
     * @param http
     * @return
     * @throws Exception 
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                .requestMatchers("/usuario", "/crearus", "/rol", "/modificarrol", "/crearemp"
                         ,"/residencial", "/modificarres", "/empleado", "/modificarus", "/modificaremp").hasRole("ADMIN")
                .requestMatchers( "/listaUsuarios", "/asignarol").hasAnyRole("EMPLOYEE","ADMIN")
                .requestMatchers("/usuariores", "/modificarusres", "/crearusres", "/crearnotificacion", "/especifica", "/general",
                         "/modificarespecifica", "/modificargeneral", "/modificarserv", "/servicio", "/verespecifica", "/vergeneral", "/enviageneral",
                         "/empleadores","/crearempres","/modificarempres","/agregarcontacto","/reporte1").hasRole("EMPLOYEE")
                .requestMatchers("/modificargestion", "/creargestion", "/verbuzon").hasRole("USER")
                .requestMatchers("/", "/perfil", "/userconfauth","/obtenerMensajes").hasAnyRole("ADMIN", "EMPLOYEE", "USER")
                .requestMatchers("/anomalia", "/crearanomalia", "/modificaranomalia", "/gestion", "/solicitud","/contacto").hasAnyRole("EMPLOYEE", "USER")
                .requestMatchers("/css/**", "/js/**", "/images/**", "/adjunto/**", "/assets/**", "/","/error").permitAll()
                .requestMatchers("/getresidenciales", "/registro", "/userconf", "/guardarcontrasena", "/recupera", "/recuperacontrasena").permitAll()
                .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                .loginPage("/login")
                .defaultSuccessUrl("/")
                .permitAll()
                )
                .logout((logout) -> logout.permitAll())
                //.exceptionHandling(new ExceptionHandlingConfigurer<>()).accessDeniedPage("/errores/403");
                .exceptionHandling(exception -> exception.authenticationEntryPoint(authenticationEntryPoint()).accessDeniedPage("/errores/403"));

        return http.build();
    }

    
}
