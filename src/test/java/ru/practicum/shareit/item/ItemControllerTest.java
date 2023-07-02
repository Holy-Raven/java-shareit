package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.util.UnionService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.practicum.shareit.util.Constant.HEADER_USER;

@WebMvcTest(controllers = UserController.class)
public class ItemControllerTest {

    @MockBean
    private ItemService itemService;

    @MockBean
    private UserService userService;

    @MockBean
    private UnionService unionService;
    
    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    private ItemDto itemDto;

    private CommentDto commentDto;

    private ItemRequest itemRequest;

    private User user;


    @BeforeEach
    void beforeEach() {

        user = User.builder()
                .id(1L)
                .name("Anna")
                .email("anna@yandex.ru")
                .build();

        itemRequest = ItemRequest.builder()
                .id(1L)
                .description("Anna")
                .requester(user)
                .created(LocalDateTime.now())
                .build();

        commentDto = CommentDto.builder()
                .id(1L)
                .text("acceptable")
                .created(LocalDateTime.now())
                .authorName("ALex")
                .build();

        itemDto = ItemDto.builder()
                .requestId(1L)
                .name("screwdriver")
                .description("works well, does not ask to eat")
                .available(true)
                .comments(List.of(commentDto))
                .requestId(itemRequest.getId())
                .build();
    }

    @Test
    void addItem() throws Exception {
        when(itemService.addItem(anyLong(), any(ItemDto.class))).thenReturn(itemDto);

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName()), String.class))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription()), String.class))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable()), String.class))
                .andExpect(jsonPath("$.requestId", is(itemDto.getRequestId()), Long.class));

        verify(itemService, times(1)).addItem(1L, itemDto);
    }

    @Test
    void updateItem() throws Exception {
        when(itemService.updateItem(any(ItemDto.class), anyLong(), anyLong())).thenReturn(itemDto);

        mvc.perform(patch("/items/{itemId}", 1L)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName()), String.class))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription()), String.class))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable()), Boolean.class))
                .andExpect(jsonPath("$.requestId", is(itemDto.getRequestId()), Long.class));

        verify(itemService, times(1)).updateItem(itemDto, 1L, 1L);
    }
}
