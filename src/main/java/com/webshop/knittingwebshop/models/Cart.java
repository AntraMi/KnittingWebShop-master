package com.webshop.knittingwebshop.models;

import lombok.Data;
import javax.persistence.*;

@Entity
@Table(name = "orders")
@Data
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String price;
    private int quantity;
    private String image;

    public Cart(int id, String name, String price, int quantity, String image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
    }

    public Cart() {}
}
