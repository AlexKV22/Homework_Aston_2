package dao;

import exception.NoDeleteUserException;
import exception.NoSaveNewUserException;
import exception.NoUpdateUserException;
import model.User;

import java.util.Optional;

public interface DAO {
    boolean create(User user) throws NoSaveNewUserException;
    boolean update(User user) throws NoUpdateUserException;
    boolean delete(Long id) throws NoDeleteUserException;
    Optional<User> read(Long id);
}
