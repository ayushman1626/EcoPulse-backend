package com.ecopulse.EcoPulse_Backend.repository;


import com.ecopulse.EcoPulse_Backend.model.Device;
import com.ecopulse.EcoPulse_Backend.model.Interface;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface DeviceRepo extends JpaRepository<Device, UUID> {

    List<Device> findByInterfaceEntityId(UUID interfaceId);

    List<Device> findByInterfaceEntityIn(List<Interface> interfaces);


    void deleteAllByInterfaceEntity(Interface iface);

    void deleteById(UUID deviceId);

    boolean existsByIdAndInterfaceEntityIn(UUID deviceId, List<Interface> interfaces);

    List<Device> findByIsActiveTrueAndLastUpdatedBefore(LocalDateTime cutoff);
}
