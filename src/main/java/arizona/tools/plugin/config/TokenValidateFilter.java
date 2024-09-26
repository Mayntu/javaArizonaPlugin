package arizona.tools.plugin.config;

import arizona.tools.plugin.services.TokenService;
import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class TokenValidateFilter extends OncePerRequestFilter {
    private final TokenService tokenService;

    public TokenValidateFilter(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String token = request.getHeader("token");
        final String hwid = request.getHeader("hwid");

        if (token == null || hwid == null) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!tokenService.isTokenValid(token, hwid)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("not valid authorization");
            return;
        }

        Authentication authentication = new TokenAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
