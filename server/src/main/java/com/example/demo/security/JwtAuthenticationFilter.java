package com.example.demo.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	@Autowired
	private JwtTokenProvider jwtTokenProvider;



	@Autowired
	private  CustomUserDetails customUserDetails;

	@Autowired
	private UserRepository userRepository;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
	        throws ServletException, IOException {

	    try {
	        // リクエストからJWTを取得
	        String jwt = jwtTokenProvider.getJwtFromReuest(request);

	        // JWTが存在し、有効であれば処理を続ける
	        if (jwt != null && jwtTokenProvider.validateToken(jwt)) {
	            // JWTからユーザー名を取得
	            String username = jwtTokenProvider.getUsernameFromJwt(jwt);
	            // UserDetailsをカスタムユーザーディテールサービスから取得
	            UserDetails user = customUserDetails.loadUserByUsername(username);
	            // ユーザーの権限を持つAuthenticationオブジェクトを作成
	            UsernamePasswordAuthenticationToken auth = 
	                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
	            // SecurityContextにAuthentication情報を設定
	            SecurityContextHolder.getContext().setAuthentication(auth);
	        }

	        // フィルターチェーンを続行
	        filterChain.doFilter(request, response);
	    } catch (RuntimeException ex) {
	        // RuntimeExceptionが発生した場合、セキュリティコンテキストをクリア
	        SecurityContextHolder.clearContext();
	        // エラーレスポンスを処理
	        handleErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
	    } catch (ServletException | IOException  e) {
	        // その他の例外が発生した場合、セキュリティコンテキストをクリア
	        SecurityContextHolder.clearContext();
	        // 一般的なエラーレスポンスを処理
	        handleErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An unexpected error occurred");
	    }
	}

	/**
	 * エラーレスポンスを処理し、指定されたステータスコードとメッセージをクライアントに返す。
	 * レスポンスのステータスコードを設定し、コンテンツタイプをプレーンテキストに設定した後、
	 * エラーメッセージをレスポンスボディに書き込む。(認証フィルター内のみで使用)
	 *
	 * @param response HTTPレスポンスオブジェクト
	 * @param status レスポンスのステータスコード
	 * @param message エラーメッセージ
	 * @throws IOException レスポンス書き込み中に発生した入出力例外
	 */
	private void handleErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
	    // レスポンスのステータスコードを設定
	    response.setStatus(status);
	    // レスポンスのコンテンツタイプをプレーンテキストに設定
	    response.setContentType("text/plain");
	    // エラーメッセージを書き込む
	    response.getWriter().write(message);
	}


}
