package com.example.githuboauth2client.service;

import com.example.githuboauth2client.entity.Provider;
import com.example.githuboauth2client.entity.UserEntity;
import com.example.githuboauth2client.repository.UserRepository;
import com.example.githuboauth2client.utils.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    public void processOAuthLogin(String username) {
        UserEntity existUser = userRepo.findByUsername(username).orElse(null);
        if (existUser.getProvider() == Provider.LOCAL) return;
        if (existUser == null) {
            UserEntity newUser = new UserEntity(username, true, Provider.GITHUB, Role.ADMIN);
            userRepo.save(newUser);
        }
    }

    public UserEntity localLogin(String username) {
        UserEntity userEntity = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username Not Found"));
        if (userEntity.getProvider() != Provider.LOCAL) throw new UsernameNotFoundException("Username Not Found");
        return userEntity;
    }

}
