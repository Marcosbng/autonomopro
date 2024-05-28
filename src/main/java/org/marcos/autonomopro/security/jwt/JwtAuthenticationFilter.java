package org.marcos.autonomopro.security.jwt;

import java.io.IOException;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    //Se ejecutará en cada petición de la API Rest (por heredar de OncePerRequestFilter) y comprobará que sea válido el token utilizando el provider
    
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService){
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest req,@NonNull HttpServletResponse res,@NonNull FilterChain filterChain) throws ServletException, IOException {
        
        final String authHeader = req.getHeader("Authorization");
        final String jwt;
        final String nickname;

        HttpServletRequest httpRequest = (HttpServletRequest) req;
        String token = null;

        if (httpRequest.getCookies() != null) {
            for (Cookie cookie : httpRequest.getCookies()) {
                if ("BearerToken".equals(cookie.getName())) {
                    token = cookie.getValue();
                }
            }
        }

        //Comprueba cabecera
        // if (authHeader==null || !authHeader.startsWith("Bearer ")) {
        //     filterChain.doFilter(req, res);
        //     return;
        // }
        // jwt = authHeader.substring(7);

        try { //hay token y lo procesamos
            nickname = jwtService.getNicknameUsuarioFromToken(token);
            //Comprueba si el token es valido para permitir el acceso al recurso
            if (nickname != null && SecurityContextHolder.getContext().getAuthentication()==null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(nickname);
                if (jwtService.isTokenValid(token, userDetails)) {
                    //Obtenemos el UserNamePasswordAuthenticationToken en base al userDetails y sus
                    //autorizaciones
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                    SecurityContextHolder.getContext().setAuthentication(authToken); //aplicamos autorizacion al contexto
                }
            }
        } catch (MalformedJwtException e) { // Si falla al autentificación 
            logger.error("Token mal formado");
        } catch (UnsupportedJwtException e) { // Si falla al autentificación 
            logger.error("Token no soportado");
        } catch (ExpiredJwtException e) { // Si falla al autentificación 
            logger.error("Token expirado");
        } catch (IllegalArgumentException e) { // Si falla al autentificación 
            logger.error("Token vacío");
        } catch (SecurityException e) { // Si falla al autentificación 
            logger.error("Fallo en la firma");
        }
        
        filterChain.doFilter(req, res);
    }

    
    
}
