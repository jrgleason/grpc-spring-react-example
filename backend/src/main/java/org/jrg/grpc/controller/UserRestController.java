package org.jrg.grpc.controller;

import java.util.List;

import org.jrg.grpc.model.UserEntity;
import org.jrg.grpc.service.UserDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserRestController {

    @Autowired
    private UserDataService userDataService;

    @GetMapping
    public List<UserEntity> getAllUsers() {
        return userDataService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserEntity getUserById(@PathVariable Long id) {
        return userDataService.getUserById(id).orElse(null);
    }

    @PostMapping
    public UserEntity createUser(@RequestBody CreateUserRequest request) {
        return userDataService.createUser(request.getName(), request.getEmail(), request.getRole());
    }

    @PutMapping("/{id}")
    public UserEntity updateUser(@PathVariable Long id, @RequestBody UpdateUserRequest request) {
        return userDataService.updateUser(id, request.getName(), request.getEmail(), request.getRole());
    }

    @DeleteMapping("/{id}")
    public boolean deleteUser(@PathVariable Long id) {
        return userDataService.deleteUser(id);
    }

    // DTOs
    public static class CreateUserRequest {
        private String name;
        private String email;
        private String role;

        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
    }

    public static class UpdateUserRequest {
        private String name;
        private String email;
        private String role;

        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
    }
}
