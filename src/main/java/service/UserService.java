package service;

import model.User;


public interface UserService {
    void create(String name, String email, Integer age);
    void update(String name, String email, Integer age, User user);
    void delete(Long id);
    User read(Long id);
}
