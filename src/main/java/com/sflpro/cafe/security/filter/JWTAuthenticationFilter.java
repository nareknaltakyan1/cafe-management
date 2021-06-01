package com.sflpro.cafe.security.filter;

import com.sflpro.cafe.util.CookieUtil;
import com.sflpro.cafe.security.JWTAuthenticationService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

@Component
public class JWTAuthenticationFilter extends GenericFilterBean {

    private final JWTAuthenticationService jwtAuthenticationService;

    public JWTAuthenticationFilter(JWTAuthenticationService jwtAuthenticationService) {
        this.jwtAuthenticationService = jwtAuthenticationService;
    }

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain filterChain) throws IOException, ServletException {

        String authToken = CookieUtil.getValue(request, JWTAuthenticationService.TOKEN_NAME);

        if (authToken != null) {
            Authentication authentication = jwtAuthenticationService.parseAuthHeader(authToken);

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

}
