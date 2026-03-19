package com.example.demo.Infrastructure.security.jwt;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;



@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter{
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    @Override

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // 1.extract the token from the request
        String header=request.getHeader("Authorization");
        log.info("Header"+header);
        String token=null;
        String userEmail=null;

        if(header!=null && header.startsWith("Bearer ")){
            token=header.substring(7);
            userEmail=jwtTokenProvider.extractUsername(token);
            log.info("userEmail"+userEmail);
        }

        if(userEmail!=null && SecurityContextHolder.getContext().getAuthentication() == null)
        {
            //2. Extract usee details from the source of truth
            UserDetails userDetails=userDetailsService.loadUserByUsername(userEmail);
            log.info("Token is valid or no"+jwtTokenProvider.validateToken(token, userDetails));
            if(jwtTokenProvider.validateToken(token, userDetails))
            {
                UsernamePasswordAuthenticationToken userauth=new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
            userauth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(userauth);
            }
        }
        filterChain.doFilter(request, response);
        
    }


}
