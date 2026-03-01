package com.example.demo.Infrastructure.persistence.user;
import com.example.demo.Domain.exceptions.DomainExceptions;
import com.example.demo.Domain.shared.Result;
import com.example.demo.Domain.user.entities.User;
import com.example.demo.Domain.shared.Error;

public class UserMapper {
    // Transform UserEntity in db into User entity in domain
    public static User toDomain(UserEntity entity){
        Result<User> userResult= User.reconstruct(
            entity.getId(),entity.getEmail(),entity.getPassword(),entity.getRole(),
            entity.isBlocked(),entity.getCreatedAt(),entity.getUpdatedAt(),entity.getUsername()
        );
        if(userResult.isFailure()){
            throw new DomainExceptions(Error.CONFLICT("Data Integrity Error: Could not reconstruct User aggregate"));
        }
        return userResult.getValue();
    }

    // Transform User entity in domain in UserEntity in db
    public static UserEntity toUserEntity(User userDomain){
        UserEntity userEntity=new UserEntity(userDomain.getUsername(),userDomain.getEmail(),userDomain.getPassword());
        if(userDomain.getId()!=null){
            userEntity.setId(userDomain.getId());
        }
        if(userDomain.getRole()!=null){
            userEntity.setRole(userDomain.getRole());
        }
        userEntity.setBlocked(userDomain.isBlocked());
        return userEntity;

    }
}
