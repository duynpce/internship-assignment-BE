package org.example.authservice.infrastructure.web;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.authservice.application.command.AuthTokenCommand;
import org.example.authservice.application.command.CallbackCommand;
import org.example.authservice.application.mapper.AuthMapper;
import org.example.authservice.application.usecase.CallbackUseCase;
import org.example.authservice.application.usecase.ExistUseCase;
import org.example.authservice.application.usecase.LogoutUseCase;
import org.example.authservice.application.usecase.RefreshTokenUseCase;
import org.example.authservice.application.usecase.RegisterUseCase;
import org.example.authservice.infrastructure.web.dto.CallbackRequest;
import org.example.authservice.infrastructure.web.dto.RegisterRequest;
import org.example.authservice.infrastructure.web.dto.ResponseDto;
import org.example.authservice.infrastructure.web.dto.TokenResponse;
import org.springframework.boot.web.server.Cookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/local")
@RequiredArgsConstructor
@Slf4j
public class LocalAuthController {

    private final RefreshTokenUseCase refreshTokenUseCase;
    private final AuthMapper authMapper;
    private final LogoutUseCase logoutUseCase;
    private final CallbackUseCase callbackUseCase;
    private final RegisterUseCase registerUseCase;
    private final ExistUseCase existUseCase;

    @PostMapping("/register")
    public ResponseEntity<ResponseDto<String>> register(@Valid @RequestBody RegisterRequest registerRequest) {
        registerUseCase.register(authMapper.toCommand(registerRequest));

        return ResponseEntity.ok(ResponseDto.success(null, "registered successfully"));
    }

    @PostMapping("/callback")
    public ResponseEntity<ResponseDto<TokenResponse>> callback(
            @RequestBody CallbackRequest request,
            HttpServletResponse response) {
        log.info("Received local callback with code: {}", request.code());

        AuthTokenCommand token = callbackUseCase.localCallback(new CallbackCommand(request.code()));
        TokenResponse tokenDto = authMapper.toDto(token);
        response.addHeader(HttpHeaders.SET_COOKIE, buildRefreshCookie(token.refreshToken()).toString());
        return ResponseEntity.ok(ResponseDto.success(tokenDto));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ResponseDto<TokenResponse>> refresh(
            @CookieValue(required = false) String refreshToken,
            HttpServletResponse response) {

        AuthTokenCommand token = refreshTokenUseCase.localRefresh(refreshToken);
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

    @GetMapping("/username/{username}")
    public ResponseEntity<ResponseDto<Boolean>> existUsername(
            @PathVariable("username") String username) {
        return ResponseEntity.ok(ResponseDto.success(existUseCase.existsByUsername(username)));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<ResponseDto<Boolean>> existEmail(
            @PathVariable("email") String email) {
        return ResponseEntity.ok(ResponseDto.success(existUseCase.existsByEmail(email)));
    }

    @GetMapping("/hello")
    public String hello() {
        return  "Hello World";
    }

    @GetMapping("/test-auth")
    public ResponseEntity<ResponseDto<String>> testAuth() {
        return ResponseEntity.ok(ResponseDto.success("test auth"));
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