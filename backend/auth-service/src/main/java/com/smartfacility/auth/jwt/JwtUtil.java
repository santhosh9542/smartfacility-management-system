package com.smartfacility.auth.jwt;

import java.util.Date;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class JwtUtil {

 private static final SecretKey key =
 Keys.hmacShaKeyFor(
 "smartfacilitysecretkeysmartfacility12345"
 .getBytes()
 );

 // GENERATE TOKEN
 public static String generateToken(
 String email,
 String role,
 Long organizationId,
 Long tenantId
 ){

   return Jwts.builder()
   .setSubject(email)
   .claim("role", role)
   .claim("organizationId", organizationId)
   .claim("tenantId", tenantId == null ? 0 : tenantId)
   .setIssuedAt(new Date())
   .setExpiration(
      new Date(
      System.currentTimeMillis()+86400000
      )
   )
   .signWith(key)
   .compact();
 }

 // EXTRACT CLAIMS
 public static Claims extractClaims(
 String token
 ){
   return Jwts.parserBuilder()
   .setSigningKey(key)
   .build()
   .parseClaimsJws(token)
   .getBody();
 }

 // VALIDATE TOKEN
 public static boolean validate(
 String token
 ){
   try{
      extractClaims(token);
      return true;
   }catch(Exception e){
      return false;
   }
 }

}