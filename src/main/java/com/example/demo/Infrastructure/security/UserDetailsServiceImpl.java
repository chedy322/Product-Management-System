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
import com.example.demo.Infrastructure.security.jwt.CustomUserDetails;
import com.example.demo.Infrastructure.security.jwt.CustomUserDetailsService;
import com.example.demo.Domain.user.entities.User;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor

// UserDetailsService interface and userDetailsSericeImpl handles loading user from db and matching it 
// to userdeatils.user 
public class UserDetailsServiceImpl implements CustomUserDetailsService {
    private final UserRepository userRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
        .map(this::mapToUserDetails)
        .orElseThrow(()->new UsernameNotFoundException("User not found"));


    }
    @Override
   public CustomUserDetails loadUserById(UUID userId) throws UsernameNotFoundException{
    return userRepository.findById(userId)
                .map(this::mapToUserDetails)
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
         user.getPassword(), 
            user.getUsername(), 
            user.getEmail(),
            true, 
            true, true,
             true);
    }
}
