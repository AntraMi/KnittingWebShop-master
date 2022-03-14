package com.webshop.knittingwebshop.repository;

import com.webshop.knittingwebshop.models.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Integer> {

    Admin findByUsername(String username);
}
