package vn.codegym.freelanceworkhub.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                // Cho phép truy cập không cần đăng nhập:
                .antMatchers("/", "/login", "/register", "/css/**", "/js/**").permitAll()
                // Các trang chỉ cho user có role cụ thể:
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/freelancer/**").hasRole("FREELANCER")
                .antMatchers("/employer/**").hasRole("EMPLOYER")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")   // custom login page
                .defaultSuccessUrl("/dashboard", true)
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/")
                .permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // Đăng nhập cứng để test nhanh
        auth.inMemoryAuthentication()
                .withUser("admin").password("{noop}123456").roles("ADMIN")
                .and()
                .withUser("freelancer").password("{noop}123456").roles("FREELANCER")
                .and()
                .withUser("employer").password("{noop}123456").roles("EMPLOYER");
    }
}
