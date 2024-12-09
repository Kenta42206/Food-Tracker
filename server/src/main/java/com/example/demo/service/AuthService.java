package com.example.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.JwtResponseDto;
import com.example.demo.dto.LoginRequestDto;
import com.example.demo.dto.SignupRequestDto;
import com.example.demo.entity.User;
import com.example.demo.exception.CustomException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtTokenProvider;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    /**
     * ユーザー登録処理を行う。
     * 指定されたユーザー名とメールアドレスが既に存在する場合、例外がスローされる。
     * ユーザー名とメールアドレスが重複しない場合、新しいユーザーを作成して保存する。
     * 
     * @param signupRequestDto ユーザー登録に必要な情報を含むDTO
     * @return {@code User} 登録されたユーザー情報
     * @throws CustomException ユーザー名またはメールアドレスが既に使用されている場合
     */
    public User signup(SignupRequestDto signupRequestDto){
        String username = signupRequestDto.getUsername();
        String email = signupRequestDto.getEmail();
        String hashedPassword = passwordEncoder.encode(signupRequestDto.getPassword());
        if(userRepository.existsByUsername(username)){
            throw new CustomException("Conflict","this username is already in use!",HttpStatus.CONFLICT);
        }

        if(userRepository.existsByEmail(email)){
        	throw new CustomException("Conflict","this email is already in use!",HttpStatus.CONFLICT);
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(hashedPassword);
        // 目標摂取栄養素の初期値として、一般平均の（P:75　F:64.4　C:280）を設定する。
        

        return userRepository.save(user);
    }

    /**
     * ユーザー認証を行い、JWTトークンを発行する。
     * 認証情報が無効な場合、認証エラーがスローされる。
     * 
     * @param loginRequestDto ログインに必要なユーザー名とパスワードを含むDTO
     * @return {@code JwtResponseDto} 発行されたJWTトークンとユーザー情報
     * @throws CustomException 認証エラー（ユーザー名またはパスワードが無効な場合）
     */
    public JwtResponseDto authenticateUser(LoginRequestDto loginRequestDto){
        try {
            Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequestDto.getUsername(), loginRequestDto.getPassword()
                )
            );
            SecurityContextHolder.getContext().setAuthentication(auth);
            String jwt = jwtTokenProvider.generateToken(auth);

            JwtResponseDto response = new JwtResponseDto();
            response.setToken(jwt);
            response.setExpiresIn(jwtTokenProvider.getExpirationMs());

            String usernameFromJwt = jwtTokenProvider.getUsernameFromJwt(jwt);

            User user = userRepository.findByUsername(usernameFromJwt).orElseThrow(()->{
                throw new ResourceNotFoundException("User not found with username {" + usernameFromJwt + "}");
            });

            response.setUser(user);
            return response;

        } catch (AuthenticationException e) {
            // 認証失敗時の処理
            throw new CustomException("Authentication Error","Invalid username or password",HttpStatus.UNAUTHORIZED);
        } 
    }
}
