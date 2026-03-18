package edu.mmcm.tradigital.service;

import edu.mmcm.tradigital.model.User;
import edu.mmcm.tradigital.repo.UserRepository;

public class UserService {
    private final UserRepository repository = new UserRepository();

    public User authenticate(String email, String password) {
        User user = repository.findByEmail(email);
        // In a real production app, you would use password hashing here
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public boolean register(String email, String password, String role) {
        // Check if user already exists
        if (repository.findByEmail(email) != null) return false;

        repository.save(new User(email, password, role));
        return true;
    }
}