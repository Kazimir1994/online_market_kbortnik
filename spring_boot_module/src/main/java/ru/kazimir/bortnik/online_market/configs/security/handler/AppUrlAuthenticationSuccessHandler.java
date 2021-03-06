package ru.kazimir.bortnik.online_market.configs.security.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;

import static ru.kazimir.bortnik.online_market.constant.RoleConstants.ADMIN_ROLE_NAME;
import static ru.kazimir.bortnik.online_market.constant.RoleConstants.CUSTOMER_ROLE_NAME;
import static ru.kazimir.bortnik.online_market.constant.RoleConstants.SALE_USER_ROLE_NAME;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.HOME_PAGE_URL;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.PUBLIC_CUSTOMER_REDIRECT_ARTICLES_SHOWING_URL;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.PUBLIC_SALE_REDIRECT_ARTICLES_SHOWING_URL;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.REDIRECT_USERS_SHOWING_URL;

public class AppUrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        handle(request, response, authentication);
        clearAuthenticationAttributes(request);
    }

    private void handle(HttpServletRequest request,
                        HttpServletResponse response, Authentication authentication) throws IOException {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String redirectURL = null;
        for (GrantedAuthority grantedAuthority : authorities) {
            switch (grantedAuthority.getAuthority()) {
                case ADMIN_ROLE_NAME: {
                    redirectURL = REDIRECT_USERS_SHOWING_URL;
                    break;
                }
                case CUSTOMER_ROLE_NAME: {
                    redirectURL = PUBLIC_CUSTOMER_REDIRECT_ARTICLES_SHOWING_URL;
                    break;
                }
                case SALE_USER_ROLE_NAME: {
                    redirectURL = PUBLIC_SALE_REDIRECT_ARTICLES_SHOWING_URL;
                    break;
                }
                default: {
                    redirectURL = HOME_PAGE_URL;
                }
            }
        }
        redirectStrategy.sendRedirect(request, response, redirectURL);
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }

}
