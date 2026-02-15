package com.example.demo.Infrastructure.persistence.user;
import com.example.demo.Domain.user.entities.User;

public class UserMapper {
    // Transform UserEntity in db into User entity in domain
    public static User toDomain(UserEntity entity){
        return User.create(
            entity.getId(),entity.getEmail(),entity.getPassword(),entity.getRole(),
            entity.getBlocked(),entity.getCreatedAt(),entity.getUpDatedAt()
        );
    }

    // Transform User entity in domain in UserEntity in db
    public static UserEntity toUserEntity(User userDomain){
        UserEntity userEntity=new UserEntity(userDomain.getEmail(),userDomain.getPassword());
        if(userDomain.getRole()!=null){
            userEntity.setRole(userDomain.getRole());
        }
        userEntity.setBlocked(userDomain.isBlocked());
        return userEntity;

    }
}
