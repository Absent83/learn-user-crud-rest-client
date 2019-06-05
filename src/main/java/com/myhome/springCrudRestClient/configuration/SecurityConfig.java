package com.myhome.springCrudRestClient.configuration;

import com.myhome.springCrudRestClient.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import javax.servlet.Filter;

/**
 * @author Nick Dolgopolov (nick_kerch@mail.ru; https://github.com/Absent83/)
 */

@Configuration
@EnableWebSecurity
@EnableOAuth2Client
//@EnableOAuth2Sso
public class SecurityConfig
        extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthProvider authProvider;

    @Qualifier("userDetailsServiceImpl")
    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @Autowired
    @Qualifier("oauth2ClientContext")
    private OAuth2ClientContext oAuth2ClientContext;

    @Autowired
    private AuthoritiesExtractor authoritiesExtractor;

    @Autowired
    PrincipalExtractor principalExtractor;


    @Bean
    @ConfigurationProperties("google.client")
    public AuthorizationCodeResourceDetails google()
    {
        return new AuthorizationCodeResourceDetails();
    }


    @Bean
    @ConfigurationProperties("google.resource")
    public ResourceServerProperties googleResource()
    {
        return new ResourceServerProperties();
    }


    @Bean
    public FilterRegistrationBean oAuth2ClientFilterRegistration(OAuth2ClientContextFilter oAuth2ClientContextFilter)
    {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(oAuth2ClientContextFilter);
        registration.setOrder(-100);
        return registration;
    }


    private Filter ssoFilter() {
        OAuth2ClientAuthenticationProcessingFilter googleFilter = new OAuth2ClientAuthenticationProcessingFilter("/login/google");
        OAuth2RestTemplate googleTemplate = new OAuth2RestTemplate(google(), oAuth2ClientContext);
        googleFilter.setRestTemplate(googleTemplate);
        CustomUserInfoTokenServices tokenServices = new CustomUserInfoTokenServices(googleResource().getUserInfoUri(), google().getClientId());
        tokenServices.setRestTemplate(googleTemplate);
        googleFilter.setTokenServices(tokenServices);
        tokenServices.setUserService(userService);
        tokenServices.setRoleService(roleService);
        tokenServices.setAuthoritiesExtractor(authoritiesExtractor);
        tokenServices.setPrincipalExtractor(principalExtractor);
        tokenServices.setPasswordEncoder(passwordEncoder());
        return googleFilter;
    }


    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/"); //не использовать цепочки фильтров (отключается Security) для указанных url (для общих ресурсов)
    }

    @Override
    protected void configure(HttpSecurity http)
            throws Exception {

        //@formatter:off
        http
                .antMatcher("/**")
                .csrf()
                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                    .and()
                .anonymous()
                    .authorities("ROLE_ANONYMOUS")
                    .principal("MyAnonimUser")
                    .and()
                .authorizeRequests()
                    .antMatchers("/", "/login", "/login/google", "/login**", "/login/*", "/webjars/**", "/error**")
                    .permitAll()
                    .and()
                .authorizeRequests()
                    .antMatchers("/users/**", "/users" , "/users/*", "/api/**", "/api/users/**", "/api/**/", "/api" , "/api/*")
                    .hasAuthority("ADMIN")
                    .and()
                .authorizeRequests()
                    .antMatchers("/profile")
                    .permitAll()
                    .and()
//                .anyRequest()
//                    .authenticated()
//                    .and()

                .formLogin()
                    .loginPage("/login")
                    .failureUrl("/login?error")
                    .defaultSuccessUrl("/profile")
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .and()
                .logout()
                    .logoutSuccessUrl("/")
                    .logoutUrl("/logout")
                    .permitAll()
                    .and();

        //@formatter:on

        http
                .addFilterBefore(ssoFilter(), UsernamePasswordAuthenticationFilter.class);
    }


    @Autowired
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        System.out.println("=== SecurityConfig === === configure AuthenticationManagerBuilder ===");
        //auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        auth.authenticationProvider(authProvider);
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}