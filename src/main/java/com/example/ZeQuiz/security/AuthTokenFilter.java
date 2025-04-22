package com.example.ZeQuiz.security;

import com.example.ZeQuiz.service.CustomUserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class AuthTokenFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtils;
    private final CustomUserDetailService userDetailsService;

    // Daftar endpoint publik (tidak perlu token)
    private final List<String> publicEndpoints = List.of(
            "/zequiz/auth/register",
            "/zequiz/auth/login"
    );

    public AuthTokenFilter(JwtUtil jwtUtils, CustomUserDetailService userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        String authHeader = request.getHeader("Authorization");

        System.out.println("🔍 FILTER ACTIVE");
        System.out.println("🔍 Request URI: " + path);
        System.out.println("🔍 Authorization Header: " + authHeader);

        // Cek jika termasuk endpoint publik
        if (path != null && publicEndpoints.stream().anyMatch(path::startsWith)) {
            System.out.println("🟢 Public endpoint — skipping filter.");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String jwt = parseJwt(request);

            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUsernameFromToken(jwt);
                System.out.println("✅ Valid token. Username: " + username);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                System.out.println("⚠️ JWT kosong atau tidak valid");
            }

        } catch (Exception e) {
            System.out.println("❌ Error saat autentikasi JWT: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7); // Ambil hanya tokennya
        }
        return null;
    }
}
