package ru.skypro.homework.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.skypro.homework.TestData;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.entity.UserEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig(classes = {UserMapperImpl.class, ImageUrlMapper.class})
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    void toUserCopiesFieldsAndBuildsImageUrl() {
        User user = userMapper.toUser(TestData.user());

        assertThat(user.getId()).isEqualTo(1);
        assertThat(user.getEmail()).isEqualTo(TestData.USER_EMAIL);
        assertThat(user.getFirstName()).isEqualTo("Ivan");
        assertThat(user.getLastName()).isEqualTo("Ivanov");
        assertThat(user.getPhone()).isEqualTo("+7 (999) 123-45-67");
        assertThat(user.getRole()).isEqualTo(Role.USER);
        assertThat(user.getImage()).isEqualTo("/images/avatar.png");
    }

    @Test
    void toUserLeavesImageEmptyWhenNoAvatar() {
        UserEntity entity = TestData.user();
        entity.setImage(null);

        assertThat(userMapper.toUser(entity).getImage()).isNull();
    }

    @Test
    void toEntityTakesEmailFromUsernameAndSkipsPassword() {
        Register register = new Register();
        register.setUsername("new@gmail.com");
        register.setPassword("password");
        register.setFirstName("Anna");
        register.setLastName("Petrova");
        register.setPhone("+79991234567");
        register.setRole(Role.ADMIN);

        UserEntity entity = userMapper.toEntity(register);

        assertThat(entity.getEmail()).isEqualTo("new@gmail.com");
        assertThat(entity.getPassword()).isNull();
        assertThat(entity.getFirstName()).isEqualTo("Anna");
        assertThat(entity.getRole()).isEqualTo(Role.ADMIN);
    }

    @Test
    void updateEntityChangesOnlyFilledFields() {
        UserEntity entity = TestData.user();
        UpdateUser updateUser = new UpdateUser();
        updateUser.setFirstName("Petr");

        userMapper.updateEntity(updateUser, entity);

        assertThat(entity.getFirstName()).isEqualTo("Petr");
        assertThat(entity.getLastName()).isEqualTo("Ivanov");
        assertThat(entity.getEmail()).isEqualTo(TestData.USER_EMAIL);
    }
}
