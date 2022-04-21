package com.example.githuboauth2client.entity;

import com.example.githuboauth2client.utils.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "tbl_user")
@Data
@NoArgsConstructor
public class UserEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    private Boolean enabled;
    private Provider provider;
    private Role role;

    public UserEntity(String username, Boolean enabled, Provider provider, Role role) {
        this.username = username;
        this.enabled = enabled;
        this.provider = provider;
        this.role = role;
    }
}
