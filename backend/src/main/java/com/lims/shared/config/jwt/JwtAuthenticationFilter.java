package com.lims.shared.config.jwt;

import com.lims.auth.UnauthorizedException;
import com.lims.shared.config.security.WebSecurityConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final  JwtService jwtService;
    private  final UserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
            final String jwtToken;
            final String userEmail;

            if (!isAuthenticatedEndPoint(request.getRequestURI())) {
                filterChain.doFilter(request, response);
                return ;
            }
            jwtToken = this.getJwtFromRequest(request);
            if (jwtToken == null) {
                throw new UnauthorizedException("Bearer Token Not exist");
//                filterChain.doFilter(request, response);
//                return;
            }
            try {
                userEmail = jwtService.extractUsername(jwtToken);
                if (userEmail != null || SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
                    if (jwtService.isTokenValid(jwtToken, userDetails)) {
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                        authToken.setDetails(
                                new WebAuthenticationDetailsSource().buildDetails(request)
                        );
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            } catch (Exception e) {
                throw new UnauthorizedException("Bearer Token is Invalid");
            }
            filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }


    private boolean isAuthenticatedEndPoint(String endpoint) {
        AntPathMatcher pathMatcher = new AntPathMatcher();
        for (String allowedEndpoint : WebSecurityConfig.ALLOWED_ENDPOINTS) {
            if (pathMatcher.match(endpoint, allowedEndpoint))
                return (false);
        }
        return (true);
    }
}
