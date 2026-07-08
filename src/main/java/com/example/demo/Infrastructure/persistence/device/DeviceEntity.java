package com.example.demo.Infrastructure.persistence.device;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.demo.Infrastructure.persistence.user.UserEntity;

import jakarta.persistence.Column;

@Entity
@Data
@Table(name="devices")
public class DeviceEntity {
    public DeviceEntity() {
    }
    public DeviceEntity(UUID deviceId, String deviceName, String deviceFingerprint, String ipadress, String userAgent, LocalDateTime createdAt,UserEntity user) {
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.deviceFingerprint = deviceFingerprint;
        this.ipadress = ipadress;
        this.userAgent = userAgent;
        this.createdAt = createdAt;
        this.user=user;

    }
    @Id
    private UUID deviceId;

    @Column(nullable = false)
    private String deviceName;

    @Column(nullable = false,unique = true)
    private String deviceFingerprint;

    @Column(nullable = false)
    private String ipadress;

    @Column(nullable = false)
    private String userAgent;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private UserEntity user;


}
