package com.example.githuboauth2client.repository;

import com.example.githuboauth2client.entity.Provider;
import com.example.githuboauth2client.entity.UserEntity;
import com.example.githuboauth2client.service.UserService;
import com.example.githuboauth2client.utils.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;


@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void addUser() {
        UserEntity userEntity = new UserEntity("abc", true, Provider.LOCAL, Role.USER);
        userEntity.setPassword(passwordEncoder.encode("abc"));
        userRepo.save(userEntity);
    }

    @Test
    public void getByNameLocal() {
        System.out.println(userService.localLogin("abc"));
    }


}