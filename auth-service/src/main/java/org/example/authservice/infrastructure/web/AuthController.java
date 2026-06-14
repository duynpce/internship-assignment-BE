package org.example.authservice.infrastructure.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.authservice.application.command.AuthTokenCommand;
import org.example.authservice.infrastructure.mapper.AuthMapper;
import org.example.authservice.infrastructure.web.dto.LoginRequest;
import org.example.authservice.infrastructure.web.dto.RegisterRequest;
import org.example.authservice.infrastructure.web.dto.ResponseDto;
import org.example.authservice.application.usecase.LoginUseCase;
import org.example.authservice.application.usecase.RefreshTokenUseCase;
import org.example.authservice.infrastructure.web.dto.TokenResponse;
import org.springframework.boot.web.server.Cookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

// infrastructure/web/AuthController.java
@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final LoginUseCase loginUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;
    private final AuthMapper authMapper;


    @PostMapping("/login")
    public ResponseEntity<ResponseDto<TokenResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthTokenCommand token = loginUseCase.login(authMapper.toCommand(request));
        TokenResponse tokenDto = authMapper.toDto(token);


        ResponseCookie cookie = ResponseCookie.from("refreshToken", token.refreshToken())
                .httpOnly(true)
                .secure(true) // Set to true in production (requires HTTPS)
                .path("/")
                .maxAge(Duration.ofDays(1))
                .sameSite(Cookie.SameSite.NONE.toString())
                .build();

        return ResponseEntity.ok(ResponseDto.success(tokenDto));
    }


    @PostMapping("/refresh")
    public ResponseEntity<ResponseDto<TokenResponse>> refresh(@CookieValue String refreshToken) {
        AuthTokenCommand token = refreshTokenUseCase.refresh(refreshToken);
        TokenResponse tokenDto = authMapper.toDto(token);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", token.refreshToken())
                .httpOnly(true)
                .secure(true) // Set to true in production (requires HTTPS)
                .path("/")
                .maxAge(Duration.ofDays(1))
                .sameSite(Cookie.SameSite.NONE.toString())
                .build();

        return ResponseEntity.ok(ResponseDto.success(tokenDto));
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello World";
    }
}