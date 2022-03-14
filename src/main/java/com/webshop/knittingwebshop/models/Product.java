package com.webshop.knittingwebshop.models;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name="products")
public class Product {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;

    @Size(min=2, message = "Name should be at least 2 characters long")
    private String name;

    private String slug;

    @Size(min=5, message = "Description should be at least 5 characters long")
    private String description;

    private String image;
    @Transient
    private MultipartFile file;

    @Pattern(regexp = "^[0-9]+([.][0-9]{1,2})?", message = "Expected format: 5, 5.99, 15, 15.99")
    private String price;

    @ManyToOne
    @Pattern(regexp = "^[1-9][0-9]*", message = "Please choose a product type")
    @JoinColumn(name="productType_id")
    private ProductType productTypeId;

    @Column(name="created_at", updatable = false )
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name="updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
