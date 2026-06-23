package com.example.demo.Application.user.listeners;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import com.example.demo.Application.user.interfaces.UserCacheInvalidationPort;
import com.example.demo.Domain.Interfaces.EmailService;
import com.example.demo.Domain.user.events.UserAuthorizationChanged;
import com.example.demo.Domain.user.events.UserDeleted;
import com.example.demo.Domain.user.events.UserPasswordChanged;
import com.example.demo.Domain.user.events.UserProfileChanged;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InvalidateCacheOnUsermodifications {
    private final UserCacheInvalidationPort userCacheInvalidationPort;
    private final EmailService emailService;
    @Async
    @EventListener
    public void handleUserProfileChanged(UserProfileChanged userProfiserProfileChanged){
        // Invalidate the cache on user profile changed for userDetails 
        userCacheInvalidationPort.invalidateUserProfile(userProfiserProfileChanged.userId());
    }

    @Async
    @EventListener
    public void handleUserAuthorizationChanged(UserAuthorizationChanged userAuthorizationChanged){
        userCacheInvalidationPort.invalidateUserAuthorization(userAuthorizationChanged.userId());

    }

    @Async
    @EventListener
    public void handleUserPasswordChanged(UserPasswordChanged userPasswordChanged){
        userCacheInvalidationPort.invalidateUserPassword(userPasswordChanged.userId());
        // send Email notofiction to user 
        // Add link to let user make logout from all devices (by deleting all refreshtokens )
        emailService.sendSimpleMail(userPasswordChanged.userEmail(),String.format("Your password have been changed.Please check if it s you who have made the action"));
    } 
    @Async 
    @EventListener
    public void handleUserDeleted(UserDeleted userDeleted){
        userCacheInvalidationPort.evictUserProfile(userDeleted.userId());
        emailService.sendSimpleMail(userDeleted.userEmail(),String.format("We are sorry to inform you that your account have been permanently deleted."));
    }



  
}
