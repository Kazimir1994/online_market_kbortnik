package ru.kazimir.bortnik.online_market.configs.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import ru.kazimir.bortnik.online_market.configs.security.handler.AppUrlAuthenticationSuccessHandler;

import static ru.kazimir.bortnik.online_market.constant.RoleConstants.ADMIN_ROLE_NAME;
import static ru.kazimir.bortnik.online_market.constant.RoleConstants.CUSTOMER_ROLE_NAME;
import static ru.kazimir.bortnik.online_market.constant.RoleConstants.SALE_USER_ROLE_NAME;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.CSS_CONTENT_URL;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.DEFAULT_PAGE_URL;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.ERROR_403_PAGE_URL;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.ERROR_500_PAGE_URL;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.HOME_PAGE_URL;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.LOGIN_PAGE_URL;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.PRIVATE_ADMIN_URL;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.PUBLIC_CUSTOMER_USER_URL;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.PUBLIC_SALE_USER_URL;

@Configuration
@Order(2)
public class WebSpringSecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;
    private final AccessDeniedHandler accessDeniedHandler;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public WebSpringSecurityConfig(@Qualifier("appUserDetailsService") UserDetailsService userDetailsService,
                                   @Qualifier("WEBAccessDeniedHandler") AccessDeniedHandler accessDeniedHandler,
                                   PasswordEncoder passwordEncoder) {
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

                .antMatchers(PRIVATE_ADMIN_URL).hasAuthority(ADMIN_ROLE_NAME)
                .antMatchers(PUBLIC_CUSTOMER_USER_URL).hasAuthority(CUSTOMER_ROLE_NAME)
                .antMatchers(PUBLIC_SALE_USER_URL).hasAuthority(SALE_USER_ROLE_NAME)

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
