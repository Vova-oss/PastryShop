package com.example.demo.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(schema = "public",name = "productPS")
public class Product {

    @Id
    @GeneratedValue
    private long id;

    private long price;

    private String typeProduct;

    private int amount;

    private String pathOfPicture;

}
