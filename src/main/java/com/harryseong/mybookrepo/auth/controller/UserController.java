package com.harryseong.mybookrepo.auth.controller;

import com.harryseong.mybookrepo.auth.AuthApplication;
import com.harryseong.mybookrepo.auth.domain.Role;
import com.harryseong.mybookrepo.auth.domain.User;
import com.harryseong.mybookrepo.auth.dto.UserDTO;
import com.harryseong.mybookrepo.auth.repository.RoleRepository;
import com.harryseong.mybookrepo.auth.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/user")
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthApplication.class);

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Method for checking if the proposed email username already exists in the db.
     * @param email: Proposed username must be a unique email address not already in db.
     * @return Boolean: Return true if the username already exist in the db.
     */
    @GetMapping("/email/{email}")
    private Boolean doesUsernameExist(@PathVariable String email) {
        return (userRepository.findByEmail(email)!=null);
    }

    /**
     * Method for saving a new user.
     * @param userDTO: UserDTO with user information.
     * @return ResponseEntity
     */
    @PostMapping("")
    private ResponseEntity<String> registerNewUser(@RequestBody @Valid UserDTO userDTO) {

        // If user already exists,
        if (userRepository.findByEmail(userDTO.getEmail()) != null) {
            LOGGER.warn(String.format("User, %s, already exists in the system.", userDTO.getEmail()));
            return new ResponseEntity<>(String.format("User, %s, already exists in the system.", userDTO.getEmail()), HttpStatus.CONFLICT);
        }

        User user = new User();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        List<Role> roles = new ArrayList<>();
        Role roleUser = roleRepository.findByName("user");
        if (roleUser != null) {
            roles.add(roleUser);
        }

        user.setRoles(roles);

        try {
            userRepository.save(user);
            LOGGER.info(String.format("User, %s, has been registered.", user.getEmail()));
            return new ResponseEntity<>(String.format("User, %s, has been registered.", user.getEmail()), HttpStatus.CREATED);
        } catch (Error e) {
            LOGGER.error(String.format("User, %s, could not be registered due to a server error.", user.getEmail()));
            return new ResponseEntity<>(String.format("User, %s, could not be registered due to a server error.", user.getEmail()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
