package com.example.demo.Infrastructure.persistence.user;
import java.lang.classfile.ClassFile.Option;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;
import com.example.demo.Infrastructure.persistence.user.UserMapper;

import com.example.demo.Domain.user.entities.User;
import com.example.demo.Domain.user.interfaces.UserRepository;


@Repository
public class UserPersistenceAdapter implements UserRepository{
    
    public JpaUserRepository jpaUserRepository;

    public UserPersistenceAdapter( JpaUserRepository jpaUserRepository){
        this.jpaUserRepository=jpaUserRepository;
    }
    @Override
    public User save(User user) {
        // map the User entity to UserEntity
        UserEntity userEntity=UserMapper.toUserEntity(user);
        // save to db
        jpaUserRepository.save(userEntity);
        return user;
    }

    @Override
    public List<User> findAll() {
        List<UserEntity> userEntities=jpaUserRepository.findAll();
        // map from UserEntity to User domain enity
       List<User> users=userEntities.stream().map(UserMapper::toDomain).toList();
       return users;
    }

    @Override
    public Optional<User> findById(UUID id) {
        Optional<UserEntity> userEntity=jpaUserRepository.findById(id);
        Optional<User> user=userEntity.map(UserMapper::toDomain);
        return user;
    }

    @Override
    public void deleteById(UUID id) {
       jpaUserRepository.deleteById(id);
        
    }

    @Override
    public Optional<User> findByEmail(String email) {
        Optional<UserEntity> userEntity=jpaUserRepository.findByEmail(email);
        Optional<User> user=userEntity.map(UserMapper::toDomain);
        return user;
    }
}
