package com.metodologia.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.util.Date;
import java.util.function.Function;
import java.security.Key;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtUtils {

    @Value("${jwt.secret.key}")
    private String secretKey;
    
    @Value("${jwt.time.expiration}")
    private String timeExpiration;

    // metdodo para crear token de acceso
    public String generateAccesToken(String username){
        return Jwts.builder()
            .setSubject(username) // enviamos el usuario que crea el token
            .setIssuedAt(new Date(System.currentTimeMillis())) // fecha que se crea el token
            .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(timeExpiration))) // fecha que se debe expirar
            .signWith(getSignatureKey(), SignatureAlgorithm.HS256) // firma que crea el token, es decir la clave con la que se crea
            .compact();

    }

    // validar token de acceso
    public boolean isTokenValid(String token){
        try{
            Jwts.parser()
                .setSigningKey(getSignatureKey()) // verifica que el token este firmado
                .build()
                .parseClaimsJws(token)
                .getBody();
            return true;
        } catch(Exception e){
            return false;
        }
    }


    //obtener username
    public String getUsernameFromToken(String token){
        return getClaim(token, Claims::getSubject);
    }

    // obtener un solo claims
    public <T> T getClaim(String token, Function<Claims, T> claimsTFunction){ // T seria un valor generico, hecho de esta forma para no limitarnos a por ejemplo un string, ya que un claim puede ser un int o lo que sea
        Claims claims = extractAllClaims(token);
        return claimsTFunction.apply(claims);
    }

    // obtener claims(datos) del token
    public Claims extractAllClaims(String token){
        return Jwts.parser()
            .setSigningKey(getSignatureKey()) // verifica que el token este firmado
            .build()
            .parseClaimsJws(token)
            .getBody();
    }


    // obtener firma del token
    public Key getSignatureKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
