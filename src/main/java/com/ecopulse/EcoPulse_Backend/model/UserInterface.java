package com.ecopulse.EcoPulse_Backend.model;


import com.ecopulse.EcoPulse_Backend.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_interface_access")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(UserInterfaceId.class)
public class UserInterface {
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = true)
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "interface_id", referencedColumnName = "id", nullable = true)
    private Interface interfaceId;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private Role role;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Interface getInterfaceId() {
        return interfaceId;
    }

    public void setInterfaceId(Interface interfaceId) {
        this.interfaceId = interfaceId;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "UserInterface{" +
                "user=" + user +
                ", interfaceId=" + interfaceId +
                ", role=" + role +
                '}';
    }
}


