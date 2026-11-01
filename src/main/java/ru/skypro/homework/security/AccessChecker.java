package ru.skypro.homework.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import ru.skypro.homework.constants.SecurityConstants;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.entity.UserEntity;

/**
 * Проверяет право изменять объявление или комментарий: это может автор или администратор.
 */
@Component
public class AccessChecker {

    private static final String ADMIN_AUTHORITY = SecurityConstants.ROLE_PREFIX + Role.ADMIN.name();

    /**
     * @param author         автор объявления или комментария
     * @param authentication текущий пользователь
     * @throws AccessDeniedException если пользователь не автор и не администратор
     */
    public void checkCanModify(UserEntity author, Authentication authentication) {
        if (!isAuthor(author, authentication) && !isAdmin(authentication)) {
            throw new AccessDeniedException(
                    "User " + authentication.getName() + " cannot modify content of user " + author.getEmail());
        }
    }

    private boolean isAuthor(UserEntity author, Authentication authentication) {
        return author.getEmail().equals(authentication.getName());
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> ADMIN_AUTHORITY.equals(authority.getAuthority()));
    }
}
