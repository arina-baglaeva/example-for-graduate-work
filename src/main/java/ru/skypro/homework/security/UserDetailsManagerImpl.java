package ru.skypro.homework.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.repository.UserRepository;

/**
 * Хранение пользователей для Spring Security в таблице users вместо памяти приложения.
 */
@Service
@RequiredArgsConstructor
public class UserDetailsManagerImpl implements UserDetailsManager {

    private final UserRepository userRepository;

    /**
     * Сохраняет нового пользователя.
     *
     * @param user пользователь, созданный через {@link SecurityUser}, с уже зашифрованным паролем
     */
    @Override
    public void createUser(UserDetails user) {
        userRepository.save(toEntity(user));
    }

    /**
     * Сохраняет изменения пользователя.
     *
     * @param user пользователь, созданный через {@link SecurityUser}
     */
    @Override
    public void updateUser(UserDetails user) {
        userRepository.save(toEntity(user));
    }

    /**
     * Удаляет пользователя по логину.
     *
     * @param username логин (email)
     */
    @Override
    @Transactional
    public void deleteUser(String username) {
        userRepository.deleteByEmail(username);
    }

    /**
     * Меняет пароль текущего авторизованного пользователя.
     *
     * @param oldPassword старый пароль, уже проверенный вызывающей стороной
     * @param newPassword новый пароль в зашифрованном виде
     */
    @Override
    public void changePassword(String oldPassword, String newPassword) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = findUser(username);
        user.setPassword(newPassword);
        userRepository.save(user);
    }

    /**
     * @param username логин (email)
     * @return true, если пользователь с таким логином есть в БД
     */
    @Override
    public boolean userExists(String username) {
        return userRepository.existsByEmail(username);
    }

    /**
     * Загружает пользователя для проверки логина и пароля.
     *
     * @param username логин (email)
     * @return данные пользователя для Spring Security
     * @throws UsernameNotFoundException если пользователь не найден
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        return new SecurityUser(findUser(username));
    }

    private UserEntity findUser(String username) {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    private UserEntity toEntity(UserDetails user) {
        if (!(user instanceof SecurityUser)) {
            throw new IllegalArgumentException("Expected SecurityUser, got " + user.getClass().getName());
        }
        return ((SecurityUser) user).getUser();
    }
}
