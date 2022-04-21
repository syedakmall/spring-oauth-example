package com.example.githuboauth2client;

import com.example.githuboauth2client.entity.UserEntity;
import com.example.githuboauth2client.repository.UserRepository;
import com.example.githuboauth2client.users.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@CrossOrigin(origins = "http://localhost:3000/")
public class HomeResources {

    @Autowired
    private UserRepository userRepo;

    @GetMapping
    public String hello() {
        return "hello";
    }

    @GetMapping("done")
    public CustomUserDetails doneLogin(Principal principalauth) {
        UserEntity userEntity = userRepo.findByUsername(principalauth.getName()).orElseThrow(() -> new IllegalStateException(""));
        return new CustomUserDetails(userEntity);
    }

    @GetMapping("cred")
    public String getCred(Principal principal){
        if(principal == null) return "none";
        return principal.getName();
    }

}
