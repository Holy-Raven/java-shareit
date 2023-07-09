package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.dto.UserDto;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class UserDtoTest {

    @Autowired
    private JacksonTester<UserDto> json;

    @Test
    void testUserDto() throws Exception {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("Anna")
                .email("anna@yandex.ru")
                .build();

        JsonContent<UserDto> result = json.write(userDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Anna");
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("anna@yandex.ru");
    }
}