package com.blogapp.backend.Configurations;

import com.blogapp.backend.Utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    private  final UserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var jwttokenOptional = getTokenFromRequest(request);
        jwttokenOptional.ifPresent(jwtToken->{
            if (JwtUtils.validateToken(jwtToken)) {
                var usernameOptional = JwtUtils.getUsernameFromToken(jwtToken);
                usernameOptional.ifPresent(username->{
                    var userDetails = userDetailsService.loadUserByUsername(username);
                    var authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null);
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                });
            }
        });
        filterChain.doFilter(request,response);
    }

    private Optional<String> getTokenFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    return Optional.of(cookie.getValue());
                }
            }
        }
        return Optional.empty();
    }
}
