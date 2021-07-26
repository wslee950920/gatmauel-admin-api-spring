package com.gatmauel.admin.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.ZonedDateTime;
import java.util.Date;

@Log4j2
@Component
public class JWTUtil {
    SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    private long expire=60*24*3;

    public String generateToken(Long id) throws Exception{
        log.debug("long id {}", id);

        return Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(expire).toInstant()))
                .claim("id", id)
                .signWith(secretKey)
                .compact();
    }

    public Long validateAndExtract(String tokenStr){
        try {
            Jws<Claims> jws = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(tokenStr);

            log.debug("jws {}", jws);
            log.debug("body {}", jws.getBody());

            Claims claims=jws.getBody();
            log.debug("claims {}", claims);

            Long id=claims.get("id", Long.class);
            log.debug("id {}", id);

            return id;
        } catch(Exception e){
            log.error(e.getMessage());

            return null;
        }
    }
}
