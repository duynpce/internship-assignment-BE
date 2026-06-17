package org.example.authservice.infrastructure.web;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.authservice.application.command.AuthTokenCommand;
import org.example.authservice.application.command.CallbackCommand;
import org.example.authservice.application.mapper.AuthTokenMapper;
import org.example.authservice.application.usecase.CallbackUseCase;
import org.example.authservice.application.usecase.LogoutUseCase;
import org.example.authservice.infrastructure.web.dto.CallbackRequest;
import org.example.authservice.infrastructure.web.dto.LoginRequest;
import org.example.authservice.infrastructure.web.dto.ResponseDto;
import org.example.authservice.application.usecase.LoginUseCase;
import org.example.authservice.application.usecase.RefreshTokenUseCase;
import org.example.authservice.infrastructure.web.dto.TokenResponse;
import org.springframework.boot.web.server.Cookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final LoginUseCase loginUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;
    private final AuthTokenMapper authTokenMapper;
    private final LogoutUseCase logoutUseCase;
    private final CallbackUseCase callbackUseCase;


    @PostMapping("/login")
    public ResponseEntity<ResponseDto<TokenResponse>> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        AuthTokenCommand token = loginUseCase.login(authTokenMapper.toCommand(request));
        TokenResponse tokenDto = authTokenMapper.toDto(token);


        response.addHeader(HttpHeaders.SET_COOKIE, buildRefreshCookie(token.refreshToken()).toString());

        return ResponseEntity.ok(ResponseDto.success(tokenDto));
    }

    @PostMapping("/callback")
    public ResponseEntity<ResponseDto<TokenResponse>> callback(
            @RequestBody CallbackRequest request,
            HttpServletResponse response) {

        AuthTokenCommand token = callbackUseCase.callback(new CallbackCommand(request.code()));
        TokenResponse tokenDto = authTokenMapper.toDto(token);

        response.addHeader(HttpHeaders.SET_COOKIE, buildRefreshCookie(token.refreshToken()).toString());

        return ResponseEntity.ok(ResponseDto.success(tokenDto));
    }


    @PostMapping("/refresh")
    public ResponseEntity<ResponseDto<TokenResponse>> refresh(@CookieValue(required = false) String refreshToken, HttpServletResponse response) {
        AuthTokenCommand token = refreshTokenUseCase.refresh(refreshToken);
        TokenResponse tokenDto = authTokenMapper.toDto(token);

        response.addHeader(HttpHeaders.SET_COOKIE, buildRefreshCookie(token.refreshToken()).toString());

        return ResponseEntity.ok(ResponseDto.success(tokenDto));
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseDto<?>> logout(@CookieValue(required = false) String refreshToken, HttpServletResponse response){
        logoutUseCase.logout(refreshToken);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite(Cookie.SameSite.NONE.toString())
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok(ResponseDto.success(null));
    }

    private ResponseCookie buildRefreshCookie(String refreshToken) {
        return ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofDays(1))
                .sameSite(Cookie.SameSite.NONE.toString())
                .build();
    }
}