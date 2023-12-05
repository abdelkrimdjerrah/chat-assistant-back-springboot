package epdbackendjava.chatassistant.Config;

import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import java.io.IOException;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
//public class JwtAuthenticationFilter  {
    @Value("${ACCESS_TOKEN_SECRET}")
    private String secretKey;


    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {



        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
        }
        else{



        jwt = authHeader.substring(7);


        try {
            Claims claims = Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(jwt).getBody();

            Map<String, String> userInfo = (Map<String, String>) claims.get("UserInfo");

            String userId = userInfo.get("userId");
            String email = userInfo.get("email");

            request.setAttribute("userId", userId);
            request.setAttribute("email", email);

        } catch (SignatureException e) {
            System.out.println("Invalid token signature: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        } catch (ExpiredJwtException e) {
            System.out.println("Token expired: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        } catch (MalformedJwtException e) {
            System.out.println("Malformed token: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        } catch (JwtException e) {
            System.out.println("JWT verification failed: " + e.getMessage());
            // General JWT verification failure, handle accordingly
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        }

        filterChain.doFilter(request, response);
    }


}
