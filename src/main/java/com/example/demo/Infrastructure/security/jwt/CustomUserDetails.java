package com.example.demo.Infrastructure.security.jwt;

import java.util.Collection;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;


@AllArgsConstructor
public class CustomUserDetails implements UserDetails {
    private final UUID userId;
    private final Collection<? extends GrantedAuthority> authorities;
    private final String password;
    private final String username;
    private final String email;
    private final boolean isAccountNonExpired;
    private final boolean isAccountNonLocked;
    private  final boolean isCredentialsNonExpired;
    private  final boolean isEnabled;
    // Custom Id field for user
    public UUID getUserId(){ return userId;}
    public String getUserEmail(){ return email;}
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){return authorities;}

    @Override
   public String getPassword(){return password;};

    @Override
   public String getUsername(){
    return username;
   };
 @Override
 public boolean isAccountNonExpired() {
     // TODO Auto-generated method stub
     return isAccountNonExpired;
 }

 @Override
 public boolean isAccountNonLocked() {
     // TODO Auto-generated method stub
     return isAccountNonLocked;
 }

 @Override
 public boolean isCredentialsNonExpired() {
     // TODO Auto-generated method stub
     return isCredentialsNonExpired;
 }

 @Override
 public boolean isEnabled() {
     // TODO Auto-generated method stub
     return isEnabled;
 }
}
