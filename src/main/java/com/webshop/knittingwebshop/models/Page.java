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
@Table(name="pages")

public class Page {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;

    @Size(min=2, message = "Title should be at least 2 characters long")
    private String title;

    private String slug;

    @Size(min=4, message="Content should be at least 4 characters long")
    private String content;

    private int sorting;

}
