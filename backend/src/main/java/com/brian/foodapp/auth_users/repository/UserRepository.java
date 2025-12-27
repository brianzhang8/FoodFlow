package com.brian.foodapp.auth_users.repository;

import com.brian.foodapp.auth_users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Since email is unique
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
