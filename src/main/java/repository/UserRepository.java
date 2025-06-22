package repository;

import model.User;

public interface UserRepository {
    void create(User user);
    void update(User user);
    void delete(int id);
    User read(int id);
}
