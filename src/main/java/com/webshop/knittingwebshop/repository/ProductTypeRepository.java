package com.webshop.knittingwebshop.repository;

import com.webshop.knittingwebshop.models.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductTypeRepository extends JpaRepository <ProductType, Integer> {

    ProductType findByName(String name);

    List<ProductType> findAll();

    ProductType findBySlug(String slug);
}