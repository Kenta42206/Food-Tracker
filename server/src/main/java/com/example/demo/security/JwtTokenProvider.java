package com.example.demo.security;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.example.demo.exception.CustomException;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtTokenProvider {


	@Value("${security.jwt.token.expiration-ms}")
	private Long jwtExpirationMs;

	@Value("${security.jwt.token.secretkey}")
	private String jwtSecretKey;

	private Key getSignWithKey(){
		byte[] decodedKey = Decoders.BASE64.decode(jwtSecretKey);
		return Keys.hmacShaKeyFor(decodedKey);
	}

	public String getUsernameFromJwt(String token){
		return Jwts.parserBuilder()
				.setSigningKey(getSignWithKey())
				.build()
				.parseClaimsJws(token)
				.getBody()
				.getSubject();
	}

	public long getExpirationMs(){
		return jwtExpirationMs;
	}

	public boolean validateToken(String token){
		try {
			// parseClaimsJws(token)でtokenを検証してくれる。
			// もし失敗したらJwtExceptionがthrowされる
			Jwts.parserBuilder().setSigningKey(getSignWithKey()).build().parseClaimsJws(token);
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			throw new CustomException("Jet error","Expired or invalid JWT token",HttpStatus.UNAUTHORIZED);
		}
	}

	// Tokenのリクエストは以下のように設定するのが一般的とのこと。
	// 
	//   Authorization: Bearer ~
	// 
	// ヘッダ情報などを取り除いたものを取得するメソッド
	public String getJwtFromReuest(HttpServletRequest req){
		String bearerToken = req.getHeader("Authorization");
		// Tokenがnullでなく、"Bearer"から始まっていれば"Bearer "を取り除く
		if(bearerToken != null && bearerToken.startsWith("Bearer ")){
			return bearerToken.substring(7);
		}
		return null;
	}


	public String generateToken(Authentication authentication){
		// 認証情報からusernameを取ってくる
		String username = authentication.getName();
		// ペイロードのissに現在のDateをセットするために使用する
		Date now = new Date();
		// ペイロードのexpに現在のDateにexpiration-msの値を足した値をセットするために使用する
		Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

		return Jwts.builder()
				.setSubject(username)
				.setIssuedAt(now)
				.setExpiration(expiryDate)
				.signWith(getSignWithKey(), SignatureAlgorithm.HS512)
				.compact();
	}
}
