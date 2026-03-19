package com.example.demo.Infrastructure.security;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.Domain.shared.Result;
import com.example.demo.Domain.user.interfaces.UserRepository;
import com.example.demo.Domain.user.entities.User;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor

// UserDetailsService interface and userDetailsSericeImpl handles loading user from db and matching it 
// to userdeatils.user 
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userResult=userRepository.findByEmail(email);
        if(userResult.isEmpty()){
             new UsernameNotFoundException("User not found");
        }
        User user=userResult.get();

        return org.springframework.security.core.userdetails.User.builder()
        .username(user.getEmail())
        .password(user.getPassword())
        .authorities(
          List.of( new  SimpleGrantedAuthority (user.getRole().getAuthority()))
        )
        .build();

    }
    
}
