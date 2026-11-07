package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.exception.InvalidPasswordException;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.UserService;

/**
 * Профиль пользователя хранится в таблице users.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;

    /**
     * {@inheritDoc}
     */
    @Override
    public User getUser(String email) {
        return userMapper.toUser(getUserEntity(email));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public UpdateUser updateUser(String email, UpdateUser updateUser) {
        UserEntity user = getUserEntity(email);
        userMapper.updateEntity(updateUser, user);
        return userMapper.toUpdateUser(userRepository.save(user));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void setPassword(String email, NewPassword newPassword) {
        UserEntity user = getUserEntity(email);
        if (!passwordEncoder.matches(newPassword.getCurrentPassword(), user.getPassword())) {
            throw new InvalidPasswordException();
        }
        user.setPassword(passwordEncoder.encode(newPassword.getNewPassword()));
        userRepository.save(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void updateUserImage(String email, MultipartFile image) {
        UserEntity user = getUserEntity(email);
        user.setImage(imageService.replace(image, user.getImage()));
        userRepository.save(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserEntity getUserEntity(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
    }
}
