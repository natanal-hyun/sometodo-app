package com.sometodo.app.auth.security;

import com.sometodo.app.auth.enums.UserRole;
import com.sometodo.app.auth.payload.response.UserResponse;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@AllArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = resolveToken(request);

        if (token != null && jwtUtil.validateToken(token)) {
            String email = jwtUtil.extractEmail(token);
            Claims claims = jwtUtil.extractClaims(token);

            UserResponse userResponse = new UserResponse(
                    email,
                    claims.get("nickname", String.class),
                    claims.get("imageUrl", String.class),
                    claims.get("statusMessage", String.class),
                    UserRole.valueOf(claims.get("role", String.class))
            );

            UserPrincipal userPrincipal = new UserPrincipal(
                    userResponse.getEmail(),
                    userResponse.getNickname(),
                    userResponse.getImageUrl(),
                    userResponse.getStatusMessage(),
                    userResponse.getRole()
            );

            Authentication auth = new UsernamePasswordAuthenticationToken(userPrincipal, token, userPrincipal.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
        } else {
            // 유효하지 않은 토큰인 경우 예외 던지기
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: Invalid or missing token");
            return;  // 더 이상 필터 체인을 진행하지 않고 응답을 종료
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
