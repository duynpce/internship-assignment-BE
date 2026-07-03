package org.example.authservice.infrastructure.web;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.authservice.application.command.AuthTokenCommand;
import org.example.authservice.application.command.CallbackCommand;
import org.example.authservice.application.mapper.AuthMapper;
import org.example.authservice.application.usecase.CallbackUseCase;
import org.example.authservice.application.usecase.LogoutUseCase;
import org.example.authservice.application.usecase.RefreshTokenUseCase;
import org.example.authservice.infrastructure.web.dto.CallbackRequest;
import org.example.authservice.infrastructure.web.dto.ResponseDto;
import org.example.authservice.infrastructure.web.dto.TokenResponse;
import org.springframework.boot.web.server.Cookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/remote")
@RequiredArgsConstructor
@Slf4j
public class RemoteAuthController {

    private final RefreshTokenUseCase refreshTokenUseCase;
    private final AuthMapper authMapper;
    private final LogoutUseCase logoutUseCase;
    private final CallbackUseCase callbackUseCase;

    @PostMapping("/callback")
    public ResponseEntity<ResponseDto<TokenResponse>> callback(
            @RequestBody CallbackRequest request,
            HttpServletResponse response) {

        AuthTokenCommand token = callbackUseCase.remoteCallback(new CallbackCommand(request.code()));
        TokenResponse tokenDto = authMapper.toDto(token);
        response.addHeader(HttpHeaders.SET_COOKIE, buildRefreshCookie(token.refreshToken()).toString());
        return ResponseEntity.ok(ResponseDto.success(tokenDto));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ResponseDto<TokenResponse>> refresh(
            @CookieValue(required = false) String refreshToken,
            HttpServletResponse response) {

        AuthTokenCommand token = refreshTokenUseCase.remoteRefresh(refreshToken);
        TokenResponse tokenDto = authMapper.toDto(token);
        response.addHeader(HttpHeaders.SET_COOKIE, buildRefreshCookie(token.refreshToken()).toString());
        return ResponseEntity.ok(ResponseDto.success(tokenDto));
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseDto<?>> logout(
            @CookieValue(required = false) String refreshToken,
            HttpServletResponse response) {

        logoutUseCase.remoteLogout(refreshToken);
        response.addHeader(HttpHeaders.SET_COOKIE, clearRefreshCookie().toString());
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

    private ResponseCookie clearRefreshCookie() {
        return ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite(Cookie.SameSite.NONE.toString())
                .build();
    }
}