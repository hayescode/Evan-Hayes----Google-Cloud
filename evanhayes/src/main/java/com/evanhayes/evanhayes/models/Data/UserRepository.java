package com.evanhayes.evanhayes.models.Data;

import com.evanhayes.evanhayes.models.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}