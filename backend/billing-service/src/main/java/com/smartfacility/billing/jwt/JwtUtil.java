package com.smartfacility.billing.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class JwtUtil {

    private static final String SECRET =
        "smartfacilitysecretkeysmartfacility12345";

    public static Claims extractClaims(
        String token
    ) {

        return Jwts.parser()
                .setSigningKey(
                    SECRET.getBytes()
                )
                .parseClaimsJws(token)
                .getBody();
    }
}