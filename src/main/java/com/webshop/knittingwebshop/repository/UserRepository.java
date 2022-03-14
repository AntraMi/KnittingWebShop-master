package com.webshop.knittingwebshop.repository;

import com.webshop.knittingwebshop.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsername(String username);
}
