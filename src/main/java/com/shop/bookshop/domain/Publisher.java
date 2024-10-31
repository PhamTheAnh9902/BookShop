package com.shop.bookshop.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "publishers")
@Setter
@Getter
public class Publisher {
    @Id
    @Column(name = "publisher_id")
    private Long publisherId;

    @Column(name = "publisher_name")
    private String publisherName;
    private String address;
    @Column(name = "phone_number")
    private String phoneNumber;
    private String email;
}
