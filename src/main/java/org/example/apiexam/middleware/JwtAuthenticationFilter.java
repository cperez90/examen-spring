package org.example.apiexam.middleware;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.apiexam.service.auth.JwtPrincipal;
import org.example.apiexam.service.auth.JwtService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    /**
     * Indiquem en quines peticions NO s'ha d'executar el filtre.
     * - /api/auth/login: no necessita JWT
     * - OPTIONS: peticions preflight de CORS
     * - com a prova, /api/movie tampoc revisarà el token
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        String method = request.getMethod();

        if ("OPTIONS".equalsIgnoreCase(method)) return true;

        if ("/auth/login".equals(path)) return true;

        if (path.startsWith("/bocata") && "GET".equalsIgnoreCase(method)) return true;

        return false;
    }

    /**
     * Aquest mètode s'executa una vegada per petició HTTP.
     * Aquí validem el token i construïm el context de seguretat.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Si ja hi ha una autenticació al context, no la sobreescrivim
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }

        // Llegim la capçalera Authorization
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        // Si no hi ha token o no és Bearer, deixem continuar la petició
        // Spring Security decidirà després si retorna 401
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extraiem el token (eliminant "Bearer ")
        String token = authHeader.substring(7);

        try {
            // Validem el token i n'extraiem l'usuari i els rols
            JwtPrincipal principal = jwtService.parseAndValidate(token);

            // Convertim els rols (ROLE_ADMIN, ROLE_USER...)
            // en authorities de Spring Security
            var authorities = principal.roles().stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .toList();

            // Creem l'objecte Authentication
            // - principal: username
            // - credentials: null (no necessitam password)
            // - authorities: rols
            var authentication = new UsernamePasswordAuthenticationToken(
                    principal.username(),
                    null,
                    authorities
            );

            // Assignem l'autenticació al context de seguretat
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Continuem amb la cadena de filtres
            filterChain.doFilter(request, response);

        } catch (JwtException e) {
            // Si el token és invàlid, expirat o no és nostre,
            // retornam 401 Unauthorized amb resposta JSON
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write("""
                {
                  "error": "el token ha expirat, és invàlid o no pertany al servidor"
                }
                """);
        }
    }
}
