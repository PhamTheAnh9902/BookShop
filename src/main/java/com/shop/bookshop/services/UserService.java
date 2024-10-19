package com.shop.bookshop.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shop.bookshop.domain.Role;
import com.shop.bookshop.domain.User;
import com.shop.bookshop.domain.Dto.UserRegistrationDto;
import com.shop.bookshop.repository.RoleRepository;
import com.shop.bookshop.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // CREATE
    public User createUser(User user) {
        return userRepository.save(user);
    }

    // DELETE
    public boolean deleteUser(long id) {
        try {
            userRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    // UPDATE
    public User getUserById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            return userOptional.get();
        }
        return null;
    }

    public User updateUser(User user) {
        User currentUser = this.getUserById(user.getId());
        if (currentUser != null) {
            currentUser.setFull_name(user.getFull_name());
            currentUser.setAddress(user.getAddress());
            currentUser.setBirth_date(user.getBirth_date());
            currentUser.setEmail(user.getEmail());
            currentUser.setPassword(user.getPassword());
            currentUser.setPhone_number(user.getPhone_number());
        }
        return currentUser;
    }

    // LIST
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // REGISTER
    public User registerDTOtoUser(UserRegistrationDto registrationDto) {
        User user = new User();

        user.setFull_name(registrationDto.getFull_name());
        user.setAddress(registrationDto.getAddress());
        user.setBirth_date(registrationDto.getBirth_date());
        user.setEmail(registrationDto.getEmail());
        user.setGender(registrationDto.getGender());

        String hashPassword = passwordEncoder.encode(registrationDto.getPassword());
        user.setPassword(hashPassword);

        Role role = roleRepository.findByName("USER");
        if (role != null) {
            user.getRoles().add(role);
        }
        userRepository.save(user);
        return user;
    }

    // LOGIN
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
