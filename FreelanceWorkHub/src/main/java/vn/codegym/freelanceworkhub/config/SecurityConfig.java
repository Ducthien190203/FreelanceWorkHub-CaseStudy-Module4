package vn.codegym.freelanceworkhub.config;

import vn.codegym.freelanceworkhub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserService userService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                // Cho phép truy cập không cần đăng nhập:
                .antMatchers("/", "/login", "/register", "/css/**", "/js/**", "/static/**").permitAll()
                // Các trang chỉ cho user có role cụ thể:
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/freelancer/**").hasRole("FREELANCER")
                .antMatchers("/employer/**").hasRole("EMPLOYER")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")   // custom login page
                .defaultSuccessUrl("/dashboard", true) // Redirect to a dashboard after successful login
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/")
                .permitAll();
        return http.build();
    }
}
