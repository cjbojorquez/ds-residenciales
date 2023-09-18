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
 *
 * @author cjbojorquez
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {// extends WebSecurityConfigurerAdapter{

    //@Autowired
    private UserDetailsService userDetailsService;
    
    @Bean 
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    
    //@Autowired
    public void configurerGlobal(AuthenticationManagerBuilder build) throws Exception{
        build.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new LoginUrlAuthenticationEntryPoint("/login"); // Puedes ajustar la URL según tus necesidades
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                .requestMatchers("/usuario", "/empleado","/modificarus","/modificaremp","/crearus","/crearemp","/listaUsuarios","/rol","/modificarrol","/asignarol"
                ,"/residencial","/modificarres").hasRole("ADMIN")
                .requestMatchers("/usuariores", "/empleado","/modificarusres","/modificaremp","/crearusres","/crearemp","/crearnotificacion","/especifica","/general"
                ,"/modificarespecifica","/modificargeneral","/modificargerstion","/modificarserv","/servicio","/verespecifica","/vergeneral","/enviageneral","/asignarol").hasAnyRole("ADMIN","EMPLOYEE")
                .requestMatchers("/modificargestion","/creargestion","/verbuzon").hasRole("USER")
                .requestMatchers("/","/perfil","/userconfigauth").hasAnyRole("ADMIN","EMPLOYEE","USER")
                .requestMatchers("/anomalia","/crearanomalia","/modificaranomalia","/gestion","solicitud").hasAnyRole("EMPLOYEE","USER")
                .requestMatchers("/css/**", "/js/**", "/images/**", "/adjunto/**", "/assets/**").permitAll()
                .requestMatchers("/getresidenciales","/registro","/userconfig","/guardarcontrasena","/recupera","/recuperacontrasena").permitAll()
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

    /*@Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }*/
}
