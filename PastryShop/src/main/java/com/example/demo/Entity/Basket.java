package com.example.demo.Entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(schema = "public",name = "basketPS")
public class Basket {

    @Id
    @GeneratedValue
    private long id;

    private long dateCreate;
    private int amount;
    private long price;
    private int available;
    private String nameOfProduct;
    private String pathOfPicture;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
