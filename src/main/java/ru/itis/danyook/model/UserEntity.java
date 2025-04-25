package ru.itis.danyook.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@Entity
@Table(name = "account")
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;
}
