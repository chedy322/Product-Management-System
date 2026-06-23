package com.example.demo.Infrastructure.security.jwt;

import java.io.IOException;
import java.util.UUID;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.Domain.Interfaces.TokensBlackList;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;



@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter{
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService userDetailsService;
    private final TokensBlackList tokensBlackList;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String userId=null;
        String token=null;
         Cookie[] cookies=request.getCookies();
            if(cookies!=null){
                for(Cookie c:cookies){
                    if("accessToken".equals(c.getName())){
                        // extract the accessToken
                        token=c.getValue();
                        break;
                        
                    }
                }
            }
        if (token==null || token.isBlank()){
            filterChain.doFilter(request, response);
            return;
        }
      
        // Check if token is BlackListed or no
        if(tokensBlackList.getBlacksListedTokenbyId(token)){
            log.warn("Blocked request is trying to use blacklisted token");
            response.setStatus(401);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Token has been revoked or logged out\"}");
            return;
        }
        // extract the useId from the token which itself include validating the token
        try{
            userId=jwtTokenProvider.extractUserID(token);
        }catch(Exception e){
            //Shouldnt be an error log 
                log.error("Invalid or expired token found in cookie: {}", e.getMessage());
        }
         
        if(userId!=null && SecurityContextHolder.getContext().getAuthentication() == null)
        {
            try{
                //2. Extract user details from the source of truth
                UUID userIdInInt=UUID.fromString(userId);
                CustomUserDetails userDetails=userDetailsService.loadUserById(userIdInInt);
                // log.info("Token is valid or no"+jwtTokenProvider.validateToken(token, userDetails));
                if(jwtTokenProvider.validateToken(token, userDetails))
                {
                UsernamePasswordAuthenticationToken userauth=new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
                userauth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(userauth);
                }
            }catch(Exception e) {
         log.error("Failed to establish user security context: {}", e.getMessage());

    }
        }
        filterChain.doFilter(request, response);
        
    }


}
