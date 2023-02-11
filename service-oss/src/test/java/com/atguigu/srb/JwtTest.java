package com.atguigu.srb;

import io.jsonwebtoken.*;
import org.junit.Test;

import java.util.Date;
import java.util.UUID;

public class JwtTest {

    private static final long tokenExpirationTime = 1000*60*60*24;
    private static final String tokenSignKey = "atguigu123";

    @Test
    public void testCreateToken() {

        JwtBuilder jwtBuilder = Jwts.builder();
        String jwt = jwtBuilder
                .setHeaderParam("alg", "HS256")
                .setHeaderParam("typ", "jwt")

                .claim("nickname", "Helen")
                .claim("avatar", "1.jpg")
                .claim("role", "admin")

                .setSubject("srb-user")
                .setIssuer("atguigu")
                .setAudience("atguigu")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpirationTime))
                .setNotBefore(new Date(System.currentTimeMillis() + 1000 * 20))
                .setId(UUID.randomUUID().toString())

                .signWith(SignatureAlgorithm.HS256, tokenSignKey)

                .compact();

        System.out.println(jwt);

    }

    @Test
    public void testGetUserInfo(){

        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6Imp3dCJ9.eyJuaWNrbmFtZSI6IkhlbGVuIiwiYXZhdGFyIjoiMS5qcGciLCJyb2xlIjoiYWRtaW4iLCJzdWIiOiJzcmItdXNlciIsImlzcyI6ImF0Z3VpZ3UiLCJhdWQiOiJhdGd1aWd1IiwiaWF0IjoxNjY4ODQwNzM1LCJleHAiOjE2Njg5MjcxMzUsIm5iZiI6MTY2ODg0MDc1NSwianRpIjoiMGQwZGViNGItYTNmNS00MDUxLTk0ZWEtZjFjNjMwYjc4NWEzIn0.-sjOYdMT2lrgyDBMIi39ob3Xo9ADDkL4pozXGz6xPHQ";

        JwtParser jwtParser = Jwts.parser();
        Jws<Claims> claimsJws = jwtParser.setSigningKey(tokenSignKey).parseClaimsJws(token);

        Claims body = claimsJws.getBody();

        String nickname = (String) body.get("nickname");
        String avatar = (String) body.get("avatar");
        String role = (String) body.get("role");
        System.out.println(nickname);
        System.out.println(avatar);
        System.out.println(role);

        String id = body.getId();
        System.out.println(id);
    }

}
