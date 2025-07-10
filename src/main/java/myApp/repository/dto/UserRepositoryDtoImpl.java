package myApp.repository.dto;

import myApp.converter.UserMapper;
import myApp.dto.dtoResponse.UserResponseDto;
import myApp.model.User;
import myApp.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryDtoImpl implements UserRepositoryDto {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserRepositoryDtoImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserResponseDto create(User user) {
        User saveUser = userRepository.save(user);
        return userMapper.entityToDto(saveUser);
    }

    @Override
    public UserResponseDto update(User user) {
        User updateUser = userRepository.save(user);
        return userMapper.entityToDto(updateUser);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserResponseDto getResponseDto(User user) {
        return userMapper.entityToDto(user);
    }

}
