package com.mallqui.todo_list_api.jwt;

import com.mallqui.todo_list_api.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {
    //6
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private int expiration;

    //Creamos el token
    public String generateToken(Authentication authentication) {
        //casteamos el usuario autenticado con userDetails para poder acceder a mas info
        UserDetails mainUser = (UserDetails) authentication.getPrincipal();
        //crea una clave secreta
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));//
        //construye y devuelve un token JWT
        return Jwts.builder()//iniciamos el proceso para crear el token los claims
                .setSubject(mainUser.getUsername())//establece el nombre de usuario subject
                .setIssuedAt(new Date())//fecha y hora en que se emite el token
                .setExpiration(new Date(new Date().getTime() + expiration * 1000L))//fecha de exp del token
                .signWith(key, SignatureAlgorithm.HS256)//firma el token con clave y usa el algoritmo HS258
                .compact();//finaliza y convierte el token en un string compacto
    }

    //Decodificamos el token para obtener el claims que es sub, exp, etc
    public Claims extractAllClaims(String token) {
        //creamos la clave secreta
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        // Creamos un parser JWT con la clave, analizamos el token, y devolvemos los claims (datos dentro del token)
        return Jwts.parserBuilder()
                .setSigningKey(key)//Le pasamos la clave para que pueda validar la firma del token
                .build()//Construimos el parser
                .parseClaimsJws(token)//Analizamos y verificamos el token recibido
                .getBody();//Obtenemos solo el cuerpo del JWT, es decir, los claims (sub, exp, etc.)
    }

    public String extractUsername(String token){
        return extractAllClaims(token).getSubject();
    }

    public Date extractExpiration(String token){
        return extractAllClaims(token).getExpiration();
    }

    public boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractUsername(token);

        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
