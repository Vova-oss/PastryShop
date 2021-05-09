package com.example.demo.Entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.transform.Source;
import java.util.List;
import java.util.Objects;


@Getter
@Setter
@Entity
@Table(schema = "public",name = "userPS")
public class User  {

    @Id
    @GeneratedValue
    private long id;

    @NotBlank(message = "Некорректные данные")
    @Size(min = 1,message = "Минимальная длина фамилии = 1")
    private String surname;

    @NotBlank(message = "Некорректные данные")
    @Size(min = 1,message = "Минимальная длина имени = 1")
    private String name;

    @NotBlank(message = "Некорректные данные")
    @Size(min = 1,message = "Минимальная длина логина = 1")
    private String login;

    @NotBlank(message = "Некорректные данные")
    @Size(min = 1,message = "Минимальная длина пароля = 1")
    private String password;

    @NotBlank(message = "Некорректный email")
    @Email(message = "Некорректный email")
    private String email;

    private String role = "ROLE_USER";

    @NotBlank(message = "Некорректные данные")
    private String telephone_number;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Basket> baskets;

    private String activationCode;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(login, user.login) && Objects.equals(password, user.password) && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, password, email);
    }
}
