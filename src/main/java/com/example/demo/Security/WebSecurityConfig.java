package com.example.demo.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource)
                .passwordEncoder(encoder())
                .usersByUsernameQuery(
                        "select login, password, '1' from userps where login=?")
                .authoritiesByUsernameQuery("select login, role from userps where login=?");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/registration").permitAll()
                .antMatchers("/registrationAction").permitAll()
                .antMatchers("/getCookie").permitAll()
                .antMatchers("/end").permitAll()
                .antMatchers("/admin/*").hasRole("ADMIN")
                .antMatchers("/products/*").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new CustomFilter("/login", authenticationManager()), UsernamePasswordAuthenticationFilter.class)
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/")
                .permitAll();
    }

//    @Override
//    public void configure(WebSecurity web)  {
//        web
//                .ignoring()
//                .antMatchers("/h2-console/**");
//    }

    @Bean
    public PasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

}
