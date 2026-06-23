package com.example.demo.Infrastructure.security;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.Domain.shared.Result;
import com.example.demo.Domain.user.interfaces.UserRepository;
import com.example.demo.Infrastructure.security.cache.dto.CachedCustomUserDetails;
import com.example.demo.Infrastructure.security.cache.interfaces.CustomUserDetailsServiceCache;
import com.example.demo.Infrastructure.security.jwt.CustomUserDetails;
import com.example.demo.Infrastructure.security.jwt.CustomUserDetailsService;
import com.example.demo.Domain.user.entities.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@RequiredArgsConstructor
@Slf4j
// UserDetailsService interface and userDetailsSericeImpl handles loading user from db and matching it 
// to userdeatils.user 
public class UserDetailsServiceImpl implements CustomUserDetailsService {
    private final UserRepository userRepository;
    private final CustomUserDetailsServiceCache customUserDetailsServiceCache;

    //Fix it later because for now we don t use it 
    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //1. Check the Redis Cache First 
    
        return userRepository.findByEmail(username)
        .map(this::mapToUserDetails)
        .orElseThrow(()->new UsernameNotFoundException("User not found"));
    }

    @Override
   public CustomUserDetails loadUserById(UUID userId) throws UsernameNotFoundException{
    try{
          //1. Check the Redis Cache first 
      Optional<CachedCustomUserDetails> cachedUserDetails=customUserDetailsServiceCache.getUserDetailsById(userId);
    //2. If cache Hit return the data
    if(cachedUserDetails!=null &&cachedUserDetails.isPresent()){
        System.out.println("cahedUserDetails "+cachedUserDetails.get());
        CachedCustomUserDetails userDetails=cachedUserDetails.get();
        return new CustomUserDetails(
        userDetails.getUserId(), 
       userDetails.getAuthorities().stream().map(SimpleGrantedAuthority::new).toList(),
        "",
            userDetails.getUsername(), 
            userDetails.getEmail(),
            true,true,
            true,
            true
            );

    };
    }catch(Exception err){
        log.warn("Redis cache hit failed or failed to deserialize: " , err.getMessage());
    }
  
    //3. If cache Miss Fallback to db and register the data to Redis Cache
    return userRepository.findById(userId) 
    .map(user->{
                    CustomUserDetails userDetails=mapToUserDetails(user);
                    try{
                        // Store the existing user data to Redis Cache
                        // create new instance of CachedCustomUserDetails
                        CachedCustomUserDetails cachedCustomUserDetails=new CachedCustomUserDetails(user.getId(), user.getUsername(), user.getEmail(), List.of(user.getRole().getAuthority()));
                        // Map the user the CustomUserDetails
                        customUserDetailsServiceCache.storeUserDetails(cachedCustomUserDetails);
                    }catch(Exception err){
                        log.warn("Failed to save to Redis cache: " , err.getMessage());
                        
                    }   
                    return userDetails;
                })
                .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + userId));
   }

    private CustomUserDetails mapToUserDetails(User user){
        // return org.springframework.security.core.userdetails.User.builder()
        // .username(user.getEmail())
        // .password(user.getPassword())
        // .authorities(
        //   List.of( new  SimpleGrantedAuthority (user.getRole().getAuthority()))
        // )
        // .build();
        return new CustomUserDetails(user.getId(), 
        List.of( new  SimpleGrantedAuthority (user.getRole().getAuthority())),
        //  user.getPassword(),
        null, 
            user.getUsername(), 
            user.getEmail(),
            true, 
            true, true,
             true);
    }
}
