package gt.com.ds.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 *
 * @author cjbojorquez
 */

@Configuration
public class SecurityConfig{// extends WebSecurityConfigurerAdapter{
    
    @Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests((requests) -> requests
				.requestMatchers("/usuario", "/empleado").hasRole("ADMIN")
                                .requestMatchers("/", "/home").hasAnyRole("ADMIN","USER")
                                .requestMatchers("/css/**", "/js/**").permitAll()
				.anyRequest().authenticated()
			)
                        
			.formLogin((form) -> form
				.loginPage("/login")
                                .defaultSuccessUrl("/")
				.permitAll()
			)
			.logout((logout) -> logout.permitAll());

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
