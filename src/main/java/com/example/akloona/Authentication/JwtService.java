package com.example.akloona.Authentication;


import com.example.akloona.Token.TokenRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;


@Service
@RequiredArgsConstructor
public class JwtService {

    private static final String Security_Key = "088TI3YCXQd26NOxZJXXKwFAIyojGuY2Y6HX7kVb9ftVp3Rz"; // At least 256 bits (32 bytes)
    private final TokenRepo tokenRepo;



    private Claims extractAllClaims(String token) {

        return Jwts.
                parserBuilder().
                setSigningKey(getSigningKey()).
                build().
                parseClaimsJws(token).
                getBody();
    }


    // This method is used to get the signing key by decoding the base64 encoded key
    public Key getSigningKey() {
        byte[] signingKey = Decoders.BASE64.decode(Security_Key);
        return Keys.hmacShaKeyFor(signingKey);
    }


    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        return Jwts.builder(). // Create a new token
                setClaims(extraClaims).// Add the extra claims
                setSubject(userDetails.getUsername()).// Set the subject for the token
                setIssuedAt(new Date(System.currentTimeMillis())).// Set the issued date to start calculating  expiration
                setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)). // 10 hours Only valid for 10 hours
                signWith(getSigningKey(), SignatureAlgorithm.HS256).
                compact();


    }

    //Overloaded method to generate token without extra claims
    public String generateToken(UserDetails userDetails) {
        return generateToken(Map.of(), userDetails);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);

        return (username.equals(userDetails.getUsername() )&& !isTokenExpired(token));
    }

    //Check if the token is expired or not by comparing the expiration date with the current date
    private boolean isTokenExpired(String token) {
        var token2 = tokenRepo.findByToken(token);
        boolean isExpired = token2.isPresent() && token2.get().isRevoked();
        return extractExpiration(token).before(new Date()) || isExpired;
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }



}
