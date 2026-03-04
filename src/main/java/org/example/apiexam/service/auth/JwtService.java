package org.example.apiexam.service.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.example.apiexam.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class JwtService {

    // Injecta la clau secreta des de application.properties. Aquesta clau s'utilitza per signar els tokens.
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    // Injecta el temps d'expiració en segons des de application.properties.
    @Value("${application.security.jwt.expiration}")
    private long expirationSeconds;

    // Injecta el nom de l'emissor (issuer) des de application.properties.
    @Value("${application.security.jwt.issuer}")
    private String issuer;

    /**
     * Genera un nou token JWT per a un usuari específic.
     *
     * @param user L'objecte usuari que conté les dades a incloure en el token.
     * @return Una cadena de text que representa el token JWT compacte.
     */
    public String generateToken(User user) {
        Date now = new Date();
        // Calcula la data d'expiració sumant els segons d'expiració (convertits a mil·lisegons) a la data actual.
        Date exp = new Date(now.getTime() + (expirationSeconds * 1000));

        // Obté la llista de rols de l'usuari.
        List<String> roles = user.getRoles();

        // Construeix el token JWT amb les seves parts (claims).
        return Jwts.builder()
                .issuer(issuer) // Estableix l'emissor del token.
                .subject(user.getName()) // Estableix el subject (el nom d'usuari).
                .issuedAt(now) // Estableix la data i hora d'emissió.
                .expiration(exp) // Estableix la data i hora d'expiració.
                .claim("roles", roles) // Afegeix informació personalitzada (claims), com els rols.
                .signWith(getSignInKey()) // Signa el token amb la clau secreta.
                .compact(); // Genera la cadena compacta del token.
    }

    /**
     * Analitza i valida un token JWT. Comprova la signatura, l'expiració i l'emissor.
     * Si el token és vàlid, retorna les dades principals de l'usuari.
     *
     * @param token La cadena del token JWT a validar.
     * @return Un objecte JwtPrincipal amb el nom d'usuari i els rols.
     * @throws JwtException Si el token és invàlid, ha expirat, té una signatura incorrecta o un emissor no vàlid.
     */
    public JwtPrincipal parseAndValidate(String token) throws JwtException {
        // Analitza el token per extreure'n les dades (claims) verificant la signatura.
        Claims claims = Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        // Validació addicional: comprova que l'emissor del token sigui el mateix que el configurat.
        String tokenIssuer = claims.getIssuer();
        if (tokenIssuer == null || !tokenIssuer.equals(issuer)) {
            throw new JwtException("Emissor del token invàlid (Invalid issuer)");
        }

        // Comprova que el subject (nom d'usuari) no estigui buit.
        String username = claims.getSubject();
        if (username == null || username.isBlank()) {
            throw new JwtException("El token no conté un subject (Missing subject)");
        }

        // Extreu els rols de forma segura, gestionant el cas que no n'hi hagi.
        List<?> rawRoles = claims.get("roles", List.class);
        List<String> roles = (rawRoles == null)
                ? List.of() // Si no hi ha rols, retorna una llista buida.
                : rawRoles.stream().map(String::valueOf).toList(); // Converteix els rols a una llista de Strings.

        // Retorna les dades de l'usuari si totes les validacions són correctes.
        return new JwtPrincipal(username, roles);
    }

    /**
     * Mètode privat per generar la clau de signatura (SecretKey) a partir de la clau secreta en format text.
     *
     * @return La clau de signatura per a l'algorisme HMAC-SHA.
     */
    private SecretKey getSignInKey() {
        // Converteix la clau secreta (String) a bytes i genera una clau segura per a l'algorisme HMAC.
        // IMPORTANT: la clau ha de ser prou llarga (>= 32 bytes per a HS256).
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }
}
