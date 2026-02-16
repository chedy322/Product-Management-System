package com.example.demo.Infrastructure.persistence.product;

import com.example.demo.Infrastructure.persistence.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaProductRepository  extends JpaRepository<ProductEntity,Long>{
    
}
