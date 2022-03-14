package com.webshop.knittingwebshop.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name="product_types")
public class ProductType {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;

    @Size(min=3, message = "Name must be at least 3 characters long")
    private String name;

    private String slug;

    private int sorting;


}
