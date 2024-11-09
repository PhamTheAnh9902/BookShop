package com.shop.bookshop.domain;

import java.sql.Date;

import com.shop.bookshop.util.constant.GenderEnum;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
@Getter
@Setter
public class User {
    public User() {
    }

    public User(String full_name, String address, Date birth_date, String email, String password, String phone_number,
                GenderEnum gender, Set<Role> roles) {
        this.full_name = full_name;
        this.address = address;
        this.birth_date = birth_date;
        this.email = email;
        this.password = password;
        this.phone_number = phone_number;
        this.gender = gender;
        this.roles = roles;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String full_name;
    private String address;
    private Date birth_date;
    private String email;
    private String password;
    private String phone_number;

    @Enumerated(EnumType.STRING)
    private GenderEnum gender;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "Users_Roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles = new HashSet<>();

    @OneToOne(mappedBy = "user")
    private Cart cart;

}
