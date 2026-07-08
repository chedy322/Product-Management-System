package com.example.demo.Domain.device.entity;

import java.util.UUID;

import com.example.demo.Domain.Primitives.Aggregate;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;



@Getter
@Setter
public class Device extends Aggregate{
    private String deviceName;
    private String deviceFingerprint;
    private String ipadress;
    private String userAgent;
    private UUID userId;

    private Device(String deviceName, String deviceFingerprint, String ipadress, String userAgent, UUID deviceId, UUID userId) {
        super(deviceId);
        this.deviceName = deviceName;
        this.deviceFingerprint = deviceFingerprint;
        this.ipadress = ipadress;
        this.userAgent = userAgent;
        this.userId = userId;
        // Register email email sent that will be sent to the user 
        // this.registerEvent();
    }


    // Reconstruct method to create a device instance from the database
    public Device reconstruct(String deviceName, String deviceFingerprint, String ipadress, String userAgent, UUID deviceId, UUID userId) {
        return new Device(deviceName, deviceFingerprint, ipadress, userAgent, deviceId, userId);
    }

    // Constructor for the device entity
    public static Device create(String deviceName, String deviceFingerprint, String ipadress, String userAgent,UUID deviceId ,UUID userId) {
        return new Device(deviceName, deviceFingerprint, ipadress, userAgent, deviceId, userId);
    }

   

    
}
