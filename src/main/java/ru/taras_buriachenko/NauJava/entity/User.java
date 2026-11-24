package ru.taras_buriachenko.NauJava.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.taras_buriachenko.NauJava.type.UserRole;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "last_seen")
    private LocalDateTime lastSeen;

    @Column(name = "is_online", nullable = false)
    private boolean isOnline = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, columnDefinition = "varchar(255) default 'USER'")
    private UserRole role;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}