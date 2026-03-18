package edu.mmcm.tradigital.service;

import edu.mmcm.tradigital.model.User;
import edu.mmcm.tradigital.repo.UserRepository;
import java.util.List;

public class UserService {
    private final UserRepository repository = new UserRepository();

    public void addUser(User user) { repository.addUser(user); }
    public List<User> getAllUsers() { return repository.getAllUsers(); }
    public void updateUser(User user) { repository.updateUser(user); }
    public void deleteUser(String id) { repository.deleteUser(id); }
}