package gt.com.ds.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
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

    

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new LoginUrlAuthenticationEntryPoint("/login"); // Puedes ajustar la URL según tus necesidades
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                .requestMatchers("/usuario", "/empleado").hasRole("ADMIN")
                .requestMatchers("/", "/home").hasAnyRole("ADMIN", "USER")
                .requestMatchers("/css/**", "/js/**", "/images/**", "/adjunto/**").permitAll()
                .requestMatchers("/getresidenciales").permitAll()
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

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }
}
