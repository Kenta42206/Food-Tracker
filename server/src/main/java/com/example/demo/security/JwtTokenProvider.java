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

	/**
     * JWTの署名に使用するキーを取得する。
     * 
     * @return {@code Key} HMAC SHAの署名に使用するキー
     */
	private Key getSignWithKey(){
		byte[] decodedKey = Decoders.BASE64.decode(jwtSecretKey);
		return Keys.hmacShaKeyFor(decodedKey);
	}

	/**
     * JWTトークンからユーザー名を取得する。
     * 
     * @param token JWTトークン
     * @return {@code String} JWTトークンに含まれるユーザー名（サブジェクト）
     */
	public String getUsernameFromJwt(String token){
		return Jwts.parserBuilder()
				.setSigningKey(getSignWithKey())
				.build()
				.parseClaimsJws(token)
				.getBody()
				.getSubject();
	}

	/**
     * JWTの有効期限（ミリ秒）を取得する。
     * 
     * @return {@code long} JWTの有効期限（ミリ秒）
     */
	public long getExpirationMs(){
		return jwtExpirationMs;
	}

	/**
     * JWTトークンを検証する。
     * トークンが無効または期限切れの場合、例外がスローされる。
     * 
     * @param token JWTトークン
     * @return {@code boolean} トークンが有効な場合はtrue、無効な場合はfalse
     * @throws CustomException JWTトークンが無効または期限切れの場合
     */
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

	/**
     * リクエストヘッダからJWTトークンを取得する。
     * 
     * @param req HTTPリクエスト
     * @return {@code String} リクエストから取得したJWTトークン、存在しない場合はnull
     */
	public String getJwtFromReuest(HttpServletRequest req){
		String bearerToken = req.getHeader("Authorization");
		// Tokenがnullでなく、"Bearer"から始まっていれば"Bearer "を取り除く
		if(bearerToken != null && bearerToken.startsWith("Bearer ")){
			return bearerToken.substring(7);
		}
		return null;
	}

	/**
     * 認証情報からJWTトークンを生成する。
     * 
     * @param authentication 認証情報
     * @return {@code String} 生成されたJWTトークン
     */
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
