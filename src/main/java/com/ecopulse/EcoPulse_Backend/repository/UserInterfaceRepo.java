package com.ecopulse.EcoPulse_Backend.repository;

import com.ecopulse.EcoPulse_Backend.model.Interface;
import com.ecopulse.EcoPulse_Backend.model.User;
import com.ecopulse.EcoPulse_Backend.model.UserInterface;
import com.ecopulse.EcoPulse_Backend.model.UserInterfaceId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserInterfaceRepo extends JpaRepository<UserInterface, UserInterfaceId> {
    Optional<List<UserInterface>> findByUser(User user);
    Optional<UserInterface> findByUserAndInterfaceId(User user, Interface interfaceEntity);
    List<UserInterface> findByInterfaceId(Interface interfaceEntity);

    void deleteAllByInterfaceId(Interface iface);
}

