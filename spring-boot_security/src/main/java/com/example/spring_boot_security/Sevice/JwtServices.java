package com.example.spring_boot_security.Sevice;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Service
public class JwtServices {

    private final String secretKey;

    public JwtServices()
    {
        secretKey = generateSecretKey();
    }

    public String generatetoken(String username)
    {
        Map<String,Object> claims = new HashMap<>();

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000*60*30))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    private Key getKey(){

        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);

    }

    public String generateSecretKey()
    {
        try {
            KeyGenerator keyGen =KeyGenerator.getInstance("HmacSHA256");
            SecretKey secretKey = keyGen.generateKey();
            System.out.println("Secret key is "+secretKey.toString());
            return Base64.getEncoder().encodeToString(secretKey.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String extractUserName(String token)
    {
        return (String) extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims,T>claimResolver)
    {
        final Claims claims = extractAllCliams(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllCliams(String token)
    {

        return Jwts.parser()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token, UserDetails userDetails)
    {
        final String username = extractUserName(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpored(token));
    }

    private boolean isTokenExpored(String token)
    {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token)
    {
    return extractClaim(token,Claims::getExpiration);
    }
}
