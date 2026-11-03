package ru.skypro.homework.entity;

import lombok.Getter;
import lombok.Setter;
import ru.skypro.homework.dto.Role;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Пользователь площадки. Логином служит email.
 */
@Getter
@Setter
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 32)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "first_name", nullable = false, length = 16)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 16)
    private String lastName;

    @Column(nullable = false, length = 20)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Role role;

    /**
     * Имя файла аватара, null если аватар не загружен.
     */
    private String image;
}
