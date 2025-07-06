package myApp.service;

import myApp.model.User;

public interface UserService {
    boolean create(String name, String email, Integer age);
    boolean update(String name, String email, Integer age, User user);
    boolean delete(Long id);
    User read(Long id);
}
