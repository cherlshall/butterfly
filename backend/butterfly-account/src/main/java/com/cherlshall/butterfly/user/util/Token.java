package com.cherlshall.butterfly.user.util;

import com.cherlshall.butterfly.util.entity.UserInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by htf on 2020/9/25.
 */
public class Token {

    private static final String SECRET = "110000";
    private static final int EXPIRE_MILLIS = 7 * 24 * 60 * 60 * 1000;
    private static final String IDENT_KEY = "ident";

    public static String create(String id, List<String> identities) {
        return Jwts.builder().setId(id).signWith(SignatureAlgorithm.HS256, SECRET)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_MILLIS))
                .claim(IDENT_KEY, String.join(",", identities))
                .compact();
    }

    public static UserInfo parse(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
            UserInfo userInfo = new UserInfo();
            userInfo.setUserName(claims.getId());
            Object ident = claims.get(IDENT_KEY);
            if (ident != null) {
                userInfo.setIdentities(Arrays.asList(ident.toString().split(",")));
            }
            return userInfo;
        } catch (Exception e) {
            return null;
        }
    }
}
