package com.example.backend.repository;

import com.example.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCase(String email);

    // ✅ Count users created after a certain date (for "New This Week")
    long countByCreatedAtAfter(Instant date);

    // ✅ Count users by role (for stats)
    long countByRole(User.Role role);
}