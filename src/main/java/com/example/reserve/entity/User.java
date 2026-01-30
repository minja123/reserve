package com.example.reserve.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter @Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private UserStatus status; // ACTIVE, DELETED, SUSPENDED

    public static User createUser(String email) {
        User user = new User();
        user.setEmail(email);
        user.status = UserStatus.ACTIVE;
        return user;
    }

    public boolean isActive() {
        return "ACTIVE".equals(this.status);
    }
}
