package com.example.demo.Infrastructure.Web;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Application.auth.LoginUseCase;
import com.example.demo.Application.auth.LogoutUseCase;
import com.example.demo.Application.auth.RefreshTokenUseCase;
import com.example.demo.Application.auth.RegisterUseCase;
import com.example.demo.Application.auth.dto.AuthTokenResponse;
import com.example.demo.Application.auth.dto.JwtPayload;
import com.example.demo.Application.auth.dto.LoginResponse;
import com.example.demo.Application.auth.dto.RefreshTokenResponse;
import com.example.demo.Application.auth.dto.UserLoginRequest;
import com.example.demo.Application.auth.dto.logout.LogoutOutput;
import com.example.demo.Application.user.dto.UserRequest;
import com.example.demo.Application.user.dto.UserResponse;
import com.example.demo.Domain.exceptions.DomainExceptions;
import com.example.demo.Domain.shared.Result;
import com.example.demo.Domain.user.entities.User;
import com.example.demo.Infrastructure.security.jwt.CustomUserDetails;
import com.example.demo.Infrastructure.security.jwt.JwtTokenProvider;

import io.jsonwebtoken.Jwt;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final LoginUseCase loginUseCase;
    private final RegisterUseCase registerUseCase;
    // private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenUseCase refreshTokenUseCase;
    private final LogoutUseCase logoutUseCase;
    // AuthController(AccessDeniedHandler accessDeniedHandler) {
    //     this.accessDeniedHandler = accessDeniedHandler;
    // }

    @PostMapping("/login")
    public ResponseEntity<AuthTokenResponse> login(@RequestBody @Valid UserLoginRequest userRequest,HttpServletResponse response){
        Result<LoginResponse> loginUserResult=loginUseCase.login(userRequest);
        if(loginUserResult.isFailure()){
            throw new DomainExceptions(loginUserResult.getError());
        }
        // 2.Get the access token and refresh token
        LoginResponse loginUser=loginUserResult.getValue();
        String accessToken=loginUser.accessToken();
        String refreshToken=loginUser.refreshToken();
        // 3. Set the cookies as access token and refresh token
        setCookies(response,accessToken,refreshToken);
        // DTO for the token
        AuthTokenResponse authTokenResponse=AuthTokenResponse.map(accessToken,refreshToken);
        
        return ResponseEntity.status(200).body(authTokenResponse);
    }
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody @Valid UserRequest userRequest){
        Result<UserResponse> registerUserResult=registerUseCase.register(userRequest);
        if(registerUserResult.isFailure()){
            throw new DomainExceptions(registerUserResult.getError());
        }
        return ResponseEntity.status(201).body(registerUserResult.getValue());
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<Map<String,String>> refreshtoken(@CookieValue(required = false,name = "refreshToken") String  oldToken) {
        if(oldToken==null||oldToken.isBlank()){
            return ResponseEntity.status(403).body(Map.of("error","Unauthorized"));
        }
        Result<RefreshTokenResponse> refreshTokenResult=refreshTokenUseCase.refreshToken(oldToken);
        if(refreshTokenResult.isFailure()){
            throw new DomainExceptions(refreshTokenResult.getError()); 
        }
        return ResponseEntity.status(200).body(Map.of("message", "Token refreshed"));
    }
    private void setCookies(HttpServletResponse response,String accessToken,String refreshToken){
        Cookie accessCookie=new Cookie("accessToken",accessToken);
        accessCookie.setMaxAge(15*60);
        accessCookie.setHttpOnly(true);
     accessCookie.setSecure(false);
     accessCookie.setPath("/");

    // Refresh Token Cookie
    Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
    refreshCookie.setHttpOnly(true);
    refreshCookie.setSecure(true);
    refreshCookie.setPath("/");
    refreshCookie.setMaxAge(604800); // 7 days

    // Add them to the response
    response.addCookie(accessCookie);
    response.addCookie(refreshCookie);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String,String>> logout(@AuthenticationPrincipal CustomUserDetails user,HttpServletResponse resposne) {
        
        if(user==null){
            return ResponseEntity.status(401).body(Map.of("error", "Not authenticated"));
        }
        Result<LogoutOutput> logoutResult=logoutUseCase.logout(user.getUserId());
        if(logoutResult.isFailure()){
              return ResponseEntity.status(logoutResult.getError().httpStatus()).body(Map.of("error",logoutResult.getError().errorMsg()));
        }
        // 2. delete the cookies
        Cookie accessCookie=new Cookie("accessToken", null);
        accessCookie.setMaxAge(0);
        accessCookie.setHttpOnly(true);
        accessCookie.setPath("/");
        accessCookie.setSecure(false);

           Cookie refreshCookie = new Cookie("refreshToken", null);
    refreshCookie.setHttpOnly(true);
    refreshCookie.setSecure(false);
    refreshCookie.setPath("/");
    refreshCookie.setMaxAge(0);

    resposne.addCookie(accessCookie);
    resposne.addCookie(refreshCookie);

        return ResponseEntity.status(200).body(Map.of("message","Logged out succesfully"));
    }
    

}
