package com.ecopulse.EcoPulse_Backend.service;

import com.ecopulse.EcoPulse_Backend.dtos.inface.InterfaceDTO;
import com.ecopulse.EcoPulse_Backend.dtos.inface.InterfaceWithDevicesDTO;
import com.ecopulse.EcoPulse_Backend.enums.Role;
import com.ecopulse.EcoPulse_Backend.model.Device;
import com.ecopulse.EcoPulse_Backend.model.Interface;
import com.ecopulse.EcoPulse_Backend.model.User;
import com.ecopulse.EcoPulse_Backend.model.UserInterface;
import com.ecopulse.EcoPulse_Backend.repository.DeviceRepo;
import com.ecopulse.EcoPulse_Backend.repository.InterfaceRepo;
import com.ecopulse.EcoPulse_Backend.repository.UserInterfaceRepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class InterfaceService {
    @Autowired
    UserService userService;

    @Autowired
    UserInterfaceRepo userInterfaceRepo;

    @Autowired
    InterfaceRepo interfaceRepo;

    @Autowired
    DeviceRepo deviceRepo;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);


    public List<InterfaceDTO> getInterfaceForUser(String username) throws UsernameNotFoundException {

        User user = userService.getUserProfile2(username);
        List<UserInterface> userInterfaces = userInterfaceRepo.findByUser(user).orElse(Collections.emptyList());

        return userInterfaces.stream()
                .map(ui -> new InterfaceDTO(ui.getInterfaceId(),ui.getRole().name()))
                .collect(Collectors.toList());
    }

    @Transactional
    public InterfaceDTO saveInterface(Interface iface, String username) throws Exception{
        User user = userService.getUserProfile2(username);
        iface.setCreatedBy(user);
        iface.setCreatedAt(LocalDateTime.now());
        Interface savedInterface = interfaceRepo.save(iface);
        UserInterface userInterfaceAccess = new UserInterface();
        userInterfaceAccess.setUser(user);
        userInterfaceAccess.setInterfaceId(savedInterface);
        userInterfaceAccess.setRole(Role.ADMIN);
        userInterfaceRepo.save(userInterfaceAccess);
        return new InterfaceDTO(savedInterface,Role.ADMIN.name());
    }

    public InterfaceWithDevicesDTO getInterfaceWithDevices(UUID interfaceId, String username) throws EntityNotFoundException{
        User user = userService.getUserProfile2(username);

        // Fetch the Interface
        Interface interfaceEntity = interfaceRepo.findById(interfaceId)
                .orElseThrow(() -> new EntityNotFoundException("Interface not found"));

        // Fetch all Devices linked to this Interface
        List<Device> devices = deviceRepo.findByInterfaceEntityId(interfaceId);

        //fetch role of current user for this Interface
        UserInterface userInterface = userInterfaceRepo.findByUserAndInterfaceId(user,interfaceEntity)
                .orElseThrow(() -> new EntityNotFoundException("Interface not found"));

        return new InterfaceWithDevicesDTO(interfaceEntity, devices, userInterface.getRole().name());
    }

    @Transactional
    public void deleteInterface(UUID interfaceId, String username, String password) {
        Interface iface = interfaceRepo.findById(interfaceId)
                .orElseThrow(() -> new RuntimeException("Interface not found"));

        if (iface.getCreatedBy() == null || !iface.getCreatedBy().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized to delete this interface");
        }

        // Validate password
        User user = userService.getUserProfile2(username);
        if (!encoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        // Delete all devices linked to this interface
        deviceRepo.deleteAllByInterfaceEntity(iface);

        //Delete User Interface Access
        userInterfaceRepo.deleteAllByInterfaceId(iface);

        // Delete the interface
        interfaceRepo.delete(iface);


    }




    private InterfaceDTO mapToDto(UserInterface ui) {
        Interface iface = ui.getInterfaceId();
        return new InterfaceDTO(
                iface.getId().toString(),
                iface.getName(),
                iface.getDescription(),
                ui.getUser().getId().toString(),
                ui.getUser().getUsername(),
                ui.getRole().name(),
                iface.getCreatedAt().toString()
        );
    }

}
