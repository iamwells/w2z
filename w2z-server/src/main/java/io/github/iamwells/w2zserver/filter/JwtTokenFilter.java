package io.github.iamwells.w2zserver;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebFilter(value = {"/*"}, filterName = "jwtTokenFilter")
@ConfigurationProperties("jwt")
public class JwtTokenFilter extends OncePerRequestFilter {

    private final List<String> whiteList = Arrays.asList("/api/login", "/api/logout");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String servletPath = request.getServletPath();
        for (String white : whiteList) {
            if (white.equals(servletPath)) {
                return;
            }
        }
    }
}
