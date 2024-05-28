package org.marcos.autonomopro.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import org.marcos.autonomopro.security.entity.UsuarioPrincipal;


@Service
public class JwtService { // Se encargará de generar el token y comprobar su validez

    @Value("${security.jwt.secret-key}") //valor en application.properties
    private String secret;
    @Value("${security.jwt.expiration-time}") //valor en application.properties
    private int expiration;

    public String generateToken(Authentication authentication) { //genera el token
        //Obtenemos el usuario principal (UserDetails)
        UsuarioPrincipal usuarioPrincipal = (UsuarioPrincipal) authentication.getPrincipal();
        //Configuramos nickname (getUsername), fecha de expedición, fecha de expiración y firmamos
        return Jwts.builder().setSubject(usuarioPrincipal.getUsername())
            .setIssuedAt(new Date())
            .setExpiration(new Date(new Date().getTime() + expiration * 1000))
            .signWith(getSignInKey(),SignatureAlgorithm.HS256)
            .compact();
    }

    public String getNicknameUsuarioFromToken(String token) { //extrae el nickname del token
        return extractClaim(token, Claims::getSubject);
    }

    public <T>T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = getNicknameUsuarioFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token,Claims::getExpiration);
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
