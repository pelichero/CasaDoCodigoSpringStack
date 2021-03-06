package br.edu.caelum.config;

import br.edu.caelum.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{

    @Autowired
    private UserDAO users;

    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth
                .userDetailsService(users)
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/products/form").hasRole("ADMIN")
                .antMatchers("/shopping/**").permitAll()
                .antMatchers(HttpMethod.POST, "/products").hasRole("ADMIN")
                .antMatchers("/products/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                //.loginPage("/login") FIXME
                .defaultSuccessUrl("/products").permitAll()
                .and()
                .logout() //It gives you an POST mehod, so the CSRF could be passed too
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login")
                .permitAll()
                .and()
                .exceptionHandling()
                .accessDeniedPage("/WEB-INF/views/errors/403.jsp");
    }

}