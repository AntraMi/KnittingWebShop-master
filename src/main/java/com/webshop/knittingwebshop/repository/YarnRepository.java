package com.webshop.knittingwebshop.repository;

import com.webshop.knittingwebshop.models.Yarn;
import org.springframework.data.jpa.repository.JpaRepository;


public interface YarnRepository extends JpaRepository<Yarn, Integer> {

    Yarn findByName(String name);


}
