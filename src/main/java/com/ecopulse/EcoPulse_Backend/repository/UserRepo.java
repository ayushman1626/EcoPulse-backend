package com.ecopulse.EcoPulse_Backend.repository;

import com.ecopulse.EcoPulse_Backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepo extends JpaRepository<User, UUID> {
    Optional<User>  findByUsername(String username);

    Optional<User> findByEmailAndIsVerifiedTrue(String email);

    Optional<User> findByUsernameAndIsVerifiedTrue(String username);

    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.email = :email ORDER BY u.createdAt DESC")
    Optional<User> findLatestByEmail(@Param("email") String email);

    List<User> findByIsVerifiedTrue();

    List<User> findTop10ByUsernameStartingWithAndIsVerifiedTrue(String prefix);

    List<User> findByIsVerifiedFalseAndCreatedAtBefore(LocalDateTime cutoff);
}

