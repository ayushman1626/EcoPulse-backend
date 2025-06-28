package com.ecopulse.EcoPulse_Backend.repository;

import com.ecopulse.EcoPulse_Backend.model.Interface;
import com.ecopulse.EcoPulse_Backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface InterfaceRepo extends JpaRepository<Interface, UUID> {
    Optional<Interface> findByCreatedBy(User user);
}
