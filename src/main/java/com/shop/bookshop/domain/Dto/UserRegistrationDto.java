package com.shop.bookshop.domain.Dto;

import java.sql.Date;

import com.shop.bookshop.services.validator.RegisterChecked;
import com.shop.bookshop.util.constant.GenderEnum;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@RegisterChecked
public class UserRegistrationDto {

    @NotBlank(message = "Không được để trống")
    private String full_name;

    @NotBlank(message = "Không được để trống")
    private String address;

    private Date birth_date;

    @NotBlank(message = "Không được để trống")
    @Email(message = "Email không hợp lệ")
    private String email;

    @NotBlank(message = "Không được để trống")
    private String password;

    @NotBlank(message = "Không được để trống")
    private String phone_number;
    private GenderEnum gender;

    public UserRegistrationDto() {
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(Date birth_date) {
        this.birth_date = birth_date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public GenderEnum getGender() {
        return gender;
    }

    public void setGender(GenderEnum gender) {
        this.gender = gender;
    }

}
