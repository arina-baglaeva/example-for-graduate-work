package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.security.SecurityUser;
import ru.skypro.homework.service.AuthService;

/**
 * Вход и регистрация через {@link UserDetailsManager}, пароли хранятся в виде BCrypt-хеша.
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserDetailsManager manager;
    private final PasswordEncoder encoder;
    private final UserMapper userMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public void login(String userName, String password) {
        if (!manager.userExists(userName)
                || !encoder.matches(password, manager.loadUserByUsername(userName).getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean register(Register register) {
        if (manager.userExists(register.getUsername())) {
            return false;
        }
        UserEntity user = userMapper.toEntity(register);
        user.setPassword(encoder.encode(register.getPassword()));
        if (user.getRole() == null) {
            user.setRole(Role.USER);
        }
        manager.createUser(new SecurityUser(user));
        return true;
    }
}
