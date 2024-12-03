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

import com.example.demo.dto.LoginRequestDto;
import com.example.demo.dto.ResponseDto;
import com.example.demo.dto.SignupRequestDto;
import com.example.demo.entity.User;
import com.example.demo.exception.CustomException;
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

        return userRepository.save(user);
    }

    public ResponseDto authenticateUser(LoginRequestDto loginRequestDto){
        try {
            Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequestDto.getUsername(), loginRequestDto.getPassword()
                )
            );
            SecurityContextHolder.getContext().setAuthentication(auth);
            String jwt = jwtTokenProvider.generateToken(auth);
            System.out.println(jwt);

            ResponseDto response = new ResponseDto();
            response.setToken(jwt);
            response.setExpiresIn(jwtTokenProvider.getExpirationMs());
            return response;

        } catch (AuthenticationException e) {
            // 認証失敗時の処理
            throw new CustomException("Authentication Error","Invalid username or password",HttpStatus.UNAUTHORIZED);
        } 
    }
}
