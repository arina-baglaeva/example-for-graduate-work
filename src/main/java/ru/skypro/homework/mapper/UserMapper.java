package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.entity.UserEntity;

/**
 * Маппинг пользователя между сущностью и DTO.
 */
@Mapper(config = MapStructConfig.class)
public interface UserMapper {

    @Mapping(target = "image", qualifiedByName = "toImageUrl")
    User toUser(UserEntity entity);

    UpdateUser toUpdateUser(UserEntity entity);

    /**
     * Пароль не копируется: перед сохранением его нужно зашифровать.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", source = "username")
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "image", ignore = true)
    UserEntity toEntity(Register register);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "image", ignore = true)
    void updateEntity(UpdateUser updateUser, @MappingTarget UserEntity entity);
}
