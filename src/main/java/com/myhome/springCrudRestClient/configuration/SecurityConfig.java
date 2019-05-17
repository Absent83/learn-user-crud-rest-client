package com.myhome.springCrudRestClient.configuration;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.Charset;

/**
 * @author Nick Dolgopolov (nick_kerch@mail.ru; https://github.com/Absent83/)
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    private final
    UserDetailsService userDetailsService;

    @Autowired
    public SecurityConfig(@Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }


    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/"); //не использовать цепочки фильтров (отключается Security) для указанных url (для общих ресурсов)
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {


        http.csrf().disable()

                //без сессий (для REST)
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()

                //порядок матчеров имее значение: сработает тот, который первый.
                //добавляет фильтр ананонимной аутентификации.
//                .authorizeRequests().antMatchers("/public").anonymous()
//                .and()

//                .authorizeRequests().antMatchers("/authorized").authenticated()
//                .and()

                .authorizeRequests()
                .antMatchers("/users/**", "/users")
                    .hasAuthority("ADMIN")
                    .and()

                .authorizeRequests()
                    .antMatchers("/profile")
                    .permitAll()
                    //.authenticated()
                    .and()

//                .authorizeRequests()
//                    .antMatchers("/profile")
//                    .anonymous()
//                    .and()

                .anonymous()
                    .authorities("ROLE_ANONYMOUS")
                    .principal("anonim")
                    .and()

                //запрашивает login-password из head (открывает окно аутентификации)
                //.httpBasic();

                .formLogin()
                    .loginPage("/login")
                    .failureUrl("/login?error")
                    .defaultSuccessUrl("/profile")
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .and()

//                .rememberMe()
//                .key("myAppKey")
//                .tokenValiditySeconds(60)
//                  .and()

                .logout()
                    .logoutUrl("/logout");
    }


    @Autowired
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        System.out.println("=== SecurityConfig === === configure AuthenticationManagerBuilder ===");
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }



    @Bean
    HttpHeaders createHeaders(){


        String plainCreds = "admin" + ":" + "adminpassword";
        byte[] plainCredsBytes = plainCreds.getBytes(Charset.forName("US-ASCII"));
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);
        String authHeader = "Basic " + base64Creds;

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", authHeader);
        return headers;
    }
}

