package com.vendor.repositories;

import com.vendor.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsersJpaRepository extends JpaRepository<User, UUID> {

    Optional<User> findByUsername(String username);
}