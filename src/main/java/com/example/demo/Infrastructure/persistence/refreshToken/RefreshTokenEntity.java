package com.example.demo.Infrastructure.persistence.refreshToken;

import com.example.demo.Infrastructure.persistence.user.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Entity 
@Table(name = "refresh_tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenEntity {

    @Id
    @Column(name = "id", updatable = false)
    private UUID id;

    @Column(name = "refresh_token", updatable = false, unique = true, nullable = false,columnDefinition = "TEXT")
    @NotEmpty(message = "Refresh token is required")
    private String refreshToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "created_at", updatable = false, nullable = false)
    private Date createdAt;
    
    @Column(name = "expires_at", nullable = false)
    private Date expireAt;

}