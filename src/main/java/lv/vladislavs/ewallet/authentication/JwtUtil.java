package lv.vladislavs.ewallet.authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lv.vladislavs.ewallet.model.dto.user.JwtUserInfo;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
public class JwtUtil {
    private final static SecretKey SIGNING_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256); //or HS384 or HS512
    /**
     * Tries to parse specified String as a JWT token.
     * If unsuccessful (token is invalid or not containing all required user properties), simply returns null.
     *
     * @param token the JWT token to parse.
     * @return the claims extracted from specified token or null if a token is invalid.
     */
    public static JwtUserInfo parseToken(String token) {
        try {
            Claims body = Jwts.parserBuilder()
                    .setSigningKey(SIGNING_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return new JwtUserInfo(body.getSubject());

        } catch (JwtException | ClassCastException e) {
            return null;
        }
    }

    /**
     * Generates a JWT token containing email as subject, other user info and role as additional claims. Tokens validity is infinite.
     *
     * @param jwtUserInfo the user for which the token will be generated.
     * @return the JWT token.
     */
    public static String generateToken(JwtUserInfo jwtUserInfo, int hoursValid) {
        Claims claims = Jwts.claims().setSubject(jwtUserInfo.getEmail());

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(Date.from(Instant.now().plus(hoursValid, ChronoUnit.HOURS)))
                .signWith(SIGNING_KEY)
                .compact();
    }
}