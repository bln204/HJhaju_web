package com.hjhaju_web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hjhaju_web.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByEmail(String email);
}
