package com.webshop.knittingwebshop.repository;

import com.webshop.knittingwebshop.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    Product findBySlug(String slug);

    Page<Product> findAll(Pageable pageable);

    List<Product> findAllByProductTypeId(String productTypeId, Pageable pageable);

    long countByProductTypeId(String productTypeId);
}
