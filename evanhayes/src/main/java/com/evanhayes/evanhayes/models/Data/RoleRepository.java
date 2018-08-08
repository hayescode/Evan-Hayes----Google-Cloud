package com.evanhayes.evanhayes.models.Data;

import com.evanhayes.evanhayes.models.User.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long>{
}
