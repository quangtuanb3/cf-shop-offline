package com.cg.model.user;

import com.cg.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name = "users")
@Accessors(chain = true)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username",unique = true, nullable = false,length = 225)
    private String username;

    @Column(name = "password_hash",nullable = false,length = 128)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    private UserRole role;


}