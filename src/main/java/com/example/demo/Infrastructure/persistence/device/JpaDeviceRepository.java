package com.example.demo.Infrastructure.persistence.device;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;


public interface JpaDeviceRepository extends JpaRepository<DeviceEntity,UUID> {
    // This class can be used to implement custom methods for the device repository if needed.
    
}

