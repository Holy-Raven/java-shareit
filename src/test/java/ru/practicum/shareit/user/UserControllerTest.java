package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    private UserDto firstUserDto;

    private  UserDto secondUserDto;

    @BeforeEach
    void beforeEach() {

        firstUserDto = UserDto.builder()
                .id(1L)
                .name("Anna")
                .email("anna@yandex.ru")
                .build();

        secondUserDto = UserDto.builder()
                .id(2L)
                .name("Sofia")
                .email("sofia@yandex.ru")
                .build();
    }

    @Test
    void addUser() throws Exception {
        when(userService.addUser(any(UserDto.class))).thenReturn(firstUserDto);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(firstUserDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(firstUserDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(firstUserDto.getName()), String.class))
                .andExpect(jsonPath("$.email", is(firstUserDto.getEmail()), String.class));

        verify(userService, times(1)).addUser(firstUserDto);
    }

    @Test
    void updateUser() throws Exception {
        when(userService.updateUser(any(UserDto.class), anyLong())).thenReturn(firstUserDto);

        mvc.perform(patch("/users/{userId}", 1L)
                        .content(mapper.writeValueAsString(firstUserDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(firstUserDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(firstUserDto.getName()), String.class))
                .andExpect(jsonPath("$.email", is(firstUserDto.getEmail()), String.class));

        verify(userService, times(1)).updateUser(firstUserDto, 1L);
    }

    @Test
    void deleteUser() throws Exception {
        mvc.perform(delete("/users/{userId}", 1L))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(1L);
    }

    @Test
    void getUser() throws Exception {
        when(userService.getUserById(1L)).thenReturn(firstUserDto);

        mvc.perform(get("/users/{userId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(firstUserDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(firstUserDto.getName()), String.class))
                .andExpect(jsonPath("$.email", is(firstUserDto.getEmail()), String.class));

        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    void getAllUsers() throws Exception {

        when(userService.getAllUsers()).thenReturn(List.of(firstUserDto, secondUserDto));

        mvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(firstUserDto, secondUserDto))));

        verify(userService, times(1)).getAllUsers();
    }
}