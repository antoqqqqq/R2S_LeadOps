package org.example.r2s_leadops.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.r2s_leadops.service.imp.JwtServiceImp;
import org.example.r2s_leadops.service.imp.CustomUserDetailsServiceImp;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Chi ap dung xac thuc JWT cho /api/v1/**. Route /hook/** (webhook ingestion)
 * duoc loai khoi filter nay ở SecurityConfig va dung co che xac thuc rieng (HMAC).
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtServiceImp jwtService;
    private final CustomUserDetailsServiceImp userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);
        try {
            Claims claims = jwtService.parseAndValidate(token);
            String email = claims.get("email", String.class);

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                System.out.println("EMAIL: " + userDetails.getUsername());
                System.out.println("AUTHORITIES: " + userDetails.getAuthorities());

                if (userDetails.isEnabled() && userDetails.isAccountNonLocked()) {
                    var authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, token, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (JwtException | IllegalArgumentException ex) {
            // Token khong hop le / het han -> khong set Authentication, request se bi 401
            // boi entry point neu endpoint yeu cau dang nhap.
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}