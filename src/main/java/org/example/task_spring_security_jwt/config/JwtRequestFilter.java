package org.example.task_spring_security_jwt.config;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.task_spring_security_jwt.exception.AuthException;
import org.example.task_spring_security_jwt.service.TokenService;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.stream.Collectors;

/**
 * Класс для конфигурации фильтра
 */

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

    public static final String BEARER_PREFIX = "Bearer ";
    public static final String HEADER_NAME = "Authorization";
    public static final String RESPONSE_INCORRECT_SIGNATURE = "Недопустимая подпись.";
    public static final String RESPONSE_INCORRECT_DURATION_TOKEN = "Время действия токена истекло.";

    private final TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader(HEADER_NAME);
        String userName = null;
        String jwtToken = null;

        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            jwtToken = authHeader.substring(BEARER_PREFIX.length());
            try {
                userName = tokenService.getUserName(jwtToken);
            } catch (SignatureException | MalformedJwtException e) {
                throw new AuthException("JWTRequestFilter: " + RESPONSE_INCORRECT_SIGNATURE + " " + e.getMessage());
            } catch (ExpiredJwtException e) {
                throw new AuthException("JWTRequestFilter: " + RESPONSE_INCORRECT_DURATION_TOKEN + " " + e.getMessage());
            }
        }
        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UsernamePasswordAuthenticationToken userNamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            userName,
                            null,
                            tokenService.getRoles(jwtToken).stream()
                                    .map(SimpleGrantedAuthority::new)
                                    .collect(Collectors.toList()));

            SecurityContextHolder.getContext().setAuthentication(userNamePasswordAuthenticationToken);
        }
        filterChain.doFilter(request, response);
    }
}
