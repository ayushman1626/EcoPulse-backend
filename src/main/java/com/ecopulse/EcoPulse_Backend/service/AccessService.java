package com.ecopulse.EcoPulse_Backend.service;

import com.ecopulse.EcoPulse_Backend.dtos.access.InterfaceAccessDTO;
import com.ecopulse.EcoPulse_Backend.enums.Role;
import com.ecopulse.EcoPulse_Backend.model.Interface;
import com.ecopulse.EcoPulse_Backend.model.User;
import com.ecopulse.EcoPulse_Backend.model.UserInterface;
import com.ecopulse.EcoPulse_Backend.repository.InterfaceRepo;
import com.ecopulse.EcoPulse_Backend.repository.UserInterfaceRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.nio.file.attribute.UserPrincipal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AccessService {

    @Autowired
    private UserService userService;

    @Autowired
    private InterfaceRepo interfaceRepo;

    @Autowired
    UserInterfaceRepo userInterfaceRepo;

    @Transactional
    public Boolean giveAccess(UUID interfaceId, String username, String role , String currentUserUsername) throws AccessDeniedException{
        User currentUser = userService.getUserProfile2(currentUserUsername);
        User newUser = userService.getUserProfile2(username);
        Interface interfaceEntity = interfaceRepo.getReferenceById(interfaceId);

        //Checking if the new user is owner of the interface or not if so throw exception stating can't modify access
        if (newUser.getId().equals(interfaceEntity.getCreatedBy().getId())) {
            throw new AccessDeniedException("You can't modify access of the owner of the interface");
        }

        //checking if the current user has admin access
        UserInterface userInterface = userInterfaceRepo
                .findByUserAndInterfaceId(currentUser, interfaceEntity)
                .orElseThrow(() -> new AccessDeniedException("Access denied to this interface"));

        if (!userInterface.getRole().equals(Role.ADMIN)) {
            throw new AccessDeniedException("Only ADMIN can give access for this interface");
        }

        //checking if user has access of the Interface already
        Optional<UserInterface> existingAccess = userInterfaceRepo
                .findByUserAndInterfaceId(newUser, interfaceEntity);

        if(existingAccess.isPresent() && existingAccess.get().getRole().equals(
                role.equalsIgnoreCase("ADMIN")?Role.ADMIN: Role.VIEWER)){
            throw new IllegalStateException("User already has Access");
        }

        //saving
        UserInterface newUserInterface = new UserInterface();
        newUserInterface.setUser(newUser);
        newUserInterface.setInterfaceId(interfaceEntity);
        newUserInterface.setRole(role.equalsIgnoreCase("ADMIN")?Role.ADMIN: Role.VIEWER);
        userInterfaceRepo.save(newUserInterface);
        return true;
    }

    public List<InterfaceAccessDTO> getAllAccess(UUID interfaceId, String currentUsername) throws AccessDeniedException{
        User currentUser = userService.getUserProfile2(currentUsername);
        Interface interfaceEntity = interfaceRepo.getById(interfaceId);

        UserInterface currentAccess = userInterfaceRepo
                .findByUserAndInterfaceId(currentUser,interfaceEntity)
                .orElseThrow(() -> new AccessDeniedException("Access denied to this interface"));

        if (!currentAccess.getRole().equals(Role.ADMIN)) {
            throw new AccessDeniedException("Only ADMIN view access for this interface");
        }

        List<UserInterface> accessList = userInterfaceRepo.findByInterfaceId(interfaceEntity);

        return accessList.stream()
                .map(ui -> new InterfaceAccessDTO(ui.getUser(),ui.getRole()))
                .collect(Collectors.toList());
    }

    //revoke access
    @Transactional
    public Boolean revokeAccess(UUID interfaceId, String username, String currentUserUsername) throws AccessDeniedException {
        User currentUser = userService.getUserProfile2(currentUserUsername);
        User newUser = userService.getUserProfile2(username);
        Interface interfaceEntity = interfaceRepo.getReferenceById(interfaceId);

        //checking if the new user is owner of the interface or not if so throw exception stating can't modify access
        if (newUser.getId().equals(interfaceEntity.getCreatedBy().getId())) {
            throw new AccessDeniedException("You can't modify access of the owner of the interface");
        }

        //checking if the current user has admin access
        UserInterface userInterface = userInterfaceRepo
                .findByUserAndInterfaceId(currentUser, interfaceEntity)
                .orElseThrow(() -> new AccessDeniedException("Access denied to this interface"));

        if (!userInterface.getRole().equals(Role.ADMIN)) {
            throw new AccessDeniedException("Only ADMIN can revoke access for this interface");
        }

        //checking if user has access of the Interface already
        Optional<UserInterface> existingAccess = userInterfaceRepo
                .findByUserAndInterfaceId(newUser, interfaceEntity);

        if(existingAccess.isEmpty()){
            throw new IllegalStateException("User doesn't have Access");
        }

        //deleting
        userInterfaceRepo.delete(existingAccess.get());
        return true;
    }
}

