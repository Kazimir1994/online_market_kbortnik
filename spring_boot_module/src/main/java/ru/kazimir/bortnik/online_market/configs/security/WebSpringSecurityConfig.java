package ru.kazimir.bortnik.online_market.configs.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import ru.kazimir.bortnik.online_market.configs.security.handler.AppUrlAuthenticationSuccessHandler;

import static ru.kazimir.bortnik.online_market.constant.RoleConstants.ADMIN_ROLE_NAME;
import static ru.kazimir.bortnik.online_market.constant.URLConstants.*;

@Configuration
public class WebSpringSecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;
    private final AccessDeniedHandler accessDeniedHandler;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public WebSpringSecurityConfig(@Qualifier("appUserDetailsService") UserDetailsService userDetailsService, AccessDeniedHandler accessDeniedHandler, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.accessDeniedHandler = accessDeniedHandler;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers(DEFAULT_PAGE_URL,
                        ERROR_403_PAGE_URL,
                        ERROR_500_PAGE_URL,
                        HOME_PAGE_URL,
                        CSS_CONTENT_URL).permitAll()
                .antMatchers(PRIVATE_URL).hasAuthority(ADMIN_ROLE_NAME)
                .anyRequest().authenticated().and()
                .formLogin().loginPage(LOGIN_PAGE_URL)
                .successHandler(authenticationSuccessHandler())
                .permitAll()
                .and()
                .logout()
                .permitAll()
                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler)
                .and()
                .csrf().disable();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new AppUrlAuthenticationSuccessHandler();
    }

    @Override
    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

}
