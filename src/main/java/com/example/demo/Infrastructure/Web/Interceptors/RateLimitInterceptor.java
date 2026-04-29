package com.example.demo.Infrastructure.Web.Interceptors;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.example.demo.Domain.Interfaces.RateLimiter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RateLimitInterceptor implements HandlerInterceptor  {
    private final RateLimiter rateLimitFilter;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
                // get the user ip adderss 
                String userIp = request.getRemoteAddr();
                if(!rateLimitFilter.isAllowed(userIp)){
                    response.setStatus(429);
                    return false;
                }
                return true;
    }
}

