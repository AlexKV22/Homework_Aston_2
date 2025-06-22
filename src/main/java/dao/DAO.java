package dao;

import model.User;

import java.util.Optional;

public interface DAO {
    void create(User user);
    void update(User user);
    void delete(Long id);
    Optional<User> read(Long id);
}
