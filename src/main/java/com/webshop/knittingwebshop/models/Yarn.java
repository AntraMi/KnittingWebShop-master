package com.webshop.knittingwebshop.models;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name="yarns")
@Data
public class Yarn {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;

    private String name;
    private String description;

    private String slug;

//    private String image;

}
