package org.jrg.grpc.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.jrg.grpc.model.UserEntity;
import org.springframework.stereotype.Service;

@Service
public class UserDataService {
    
    private final Map<Long, UserEntity> users = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public UserDataService() {
        // Initialize with some sample data
        initializeSampleData();
    }
    
    private void initializeSampleData() {
        createUser("John Doe", "john.doe@example.com", "ADMIN");
        createUser("Jane Smith", "jane.smith@example.com", "USER");
        createUser("Bob Johnson", "bob.johnson@example.com", "USER");
        createUser("Alice Brown", "alice.brown@example.com", "MODERATOR");
    }

    public UserEntity createUser(String name, String email, String role) {
        Long id = idGenerator.getAndIncrement();
        UserEntity user = new UserEntity(id, name, email, role, Instant.now());
        users.put(id, user);
        return user;
    }

    public Optional<UserEntity> getUserById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    public List<UserEntity> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public UserEntity updateUser(Long id, String name, String email, String role) {
        UserEntity existingUser = users.get(id);
        if (existingUser != null) {
            existingUser.setName(name);
            existingUser.setEmail(email);
            existingUser.setRole(role);
            return existingUser;
        }
        return null;
    }

    public boolean deleteUser(Long id) {
        return users.remove(id) != null;
    }

    public int getUserCount() {
        return users.size();
    }
}
