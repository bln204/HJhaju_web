package com.hjhaju_web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hjhaju_web.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    List<User> findAll();
}
