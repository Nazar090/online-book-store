package com.example.onlinebookstore.service;

import com.example.onlinebookstore.dto.user.UserRegistrationRequestDto;
import com.example.onlinebookstore.dto.user.UserResponseDto;
import com.example.onlinebookstore.exception.RegistrationException;
import com.example.onlinebookstore.mapper.UserMapper;
import com.example.onlinebookstore.model.User;
import com.example.onlinebookstore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new RegistrationException("User with this email already exists");
        }
        User user = userMapper.toUserEntity(requestDto);
        userRepository.save(user);
        return userMapper.toDo(user);
    }
}
