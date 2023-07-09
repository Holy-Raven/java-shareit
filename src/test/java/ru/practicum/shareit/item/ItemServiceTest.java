package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.util.UnionService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ItemServiceTest {

    @Autowired
    private ItemService itemService;

    @MockBean
    private ItemRepository itemRepository;

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private BookingRepository bookingRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ItemRequestRepository itemRequestRepository;

    @MockBean
    private UnionService unionService;

    private User user;
    private Item item;
    private ItemDto itemDto;
    private Comment comment;
    private CommentDto commentDto;
    private ItemRequest itemRequest;
    private Booking firstBooking;
    private Booking secondBooking;

    @BeforeEach
    void beforeEach() {
        user = User.builder()
                .id(1L)
                .name("Anna")
                .email("anna@yandex.ru")
                .build();

        itemRequest = ItemRequest.builder()
                .id(1L)
                .description("ItemRequest 1")
                .created(LocalDateTime.now())
                .build();

        item = Item.builder()
                .id(1L)
                .name("screwdriver")
                .description("works well, does not ask to eat")
                .available(true)
                .owner(user)
                .request(itemRequest)
                .build();

        itemDto = ItemMapper.returnItemDto(item);

        comment = Comment.builder()
                        .id(1L)
                        .author(user)
                        .created(LocalDateTime.now())
                        .text("Thx, Cool item")
                        .build();

        commentDto = CommentMapper.returnCommentDto(comment);

        firstBooking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now())
                .item(item)
                .booker(user)
                .status(Status.APPROVED)
                .build();

        secondBooking = Booking.builder()
                .id(2L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now())
                .item(item)
                .booker(user)
                .status(Status.APPROVED)
                .build();
    }

    @Test
    void addItem() {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.ofNullable(itemRequest));
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        ItemDto itemDtoTest = itemService.addItem(user.getId(), itemDto);

        assertEquals(itemDtoTest.getId(), itemDto.getId());
        assertEquals(itemDtoTest.getDescription(), itemDto.getDescription());

        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void updateItem() {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.existsById(anyLong())).thenReturn(true);
        when(itemRepository.findByOwnerId(anyLong())).thenReturn(List.of(item));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        ItemDto itemDtoTest = itemService.updateItem(itemDto, item.getId(), user.getId());

        assertEquals(itemDtoTest.getId(), itemDto.getId());
        assertEquals(itemDtoTest.getDescription(), itemDto.getDescription());

        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void updateItemNotBelongUser() {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.existsById(anyLong())).thenReturn(true);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        when(itemRepository.findByOwnerId(anyLong())).thenReturn(Collections.emptyList());

        assertThrows(NotFoundException.class, () -> itemService.updateItem(itemDto, item.getId(), user.getId()));
    }

    @Test
    void getItemById() {
        when(itemRepository.existsById(anyLong())).thenReturn(true);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findFirstByItemIdAndStatusAndStartBeforeOrderByStartDesc(item.getId(), Status.APPROVED, LocalDateTime.now())).thenReturn(Optional.of(firstBooking));
        when(bookingRepository.findFirstByItemIdAndStatusAndStartAfterOrderByStartAsc(item.getId(), Status.APPROVED, LocalDateTime.now())).thenReturn(Optional.of(secondBooking));
        when(commentRepository.findAllByItemId(anyLong())).thenReturn(List.of(comment));

        ItemDto itemDtoTest = itemService.getItemById(item.getId(), user.getId());

        assertEquals(itemDtoTest.getId(), item.getId());
        assertEquals(itemDtoTest.getDescription(), item.getDescription());
        assertEquals(itemDtoTest.getAvailable(), item.getAvailable());
        assertEquals(itemDtoTest.getRequestId(), item.getRequest().getId());

        verify(itemRepository, times(1)).findById(anyLong());
    }

    @Test
    void getItemsUser() {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(unionService.checkPageSize(anyInt(), anyInt())).thenReturn(PageRequest.of(5 / 10,10));
        when(itemRepository.findByOwnerId(anyLong(), any(PageRequest.class))).thenReturn(new PageImpl<>(List.of(item)));
        when(bookingRepository.findFirstByItemIdAndStatusAndStartBeforeOrderByStartDesc(item.getId(), Status.APPROVED, LocalDateTime.now())).thenReturn(Optional.of(firstBooking));
        when(bookingRepository.findFirstByItemIdAndStatusAndStartAfterOrderByStartAsc(item.getId(), Status.APPROVED, LocalDateTime.now())).thenReturn(Optional.of(secondBooking));
        when(commentRepository.findAllByItemId(anyLong())).thenReturn(List.of(comment));

        ItemDto itemDtoTest = itemService.getItemsUser(user.getId(), 5, 10).get(0);

        assertEquals(itemDtoTest.getId(), item.getId());
        assertEquals(itemDtoTest.getDescription(), item.getDescription());
        assertEquals(itemDtoTest.getAvailable(), item.getAvailable());
        assertEquals(itemDtoTest.getRequestId(), item.getRequest().getId());

        verify(itemRepository, times(1)).findByOwnerId(anyLong(), any(PageRequest.class));
    }

    @Test
    void searchItem() {
        when(unionService.checkPageSize(anyInt(), anyInt())).thenReturn(PageRequest.of(5 / 10,10));
        when(itemRepository.search(anyString(), any(PageRequest.class))).thenReturn(new ArrayList<>(List.of(item)));

        ItemDto itemDtoTest = itemService.searchItem("text", 5, 10).get(0);

        assertEquals(itemDtoTest.getId(), item.getId());
        assertEquals(itemDtoTest.getDescription(), item.getDescription());
        assertEquals(itemDtoTest.getAvailable(), item.getAvailable());
        assertEquals(itemDtoTest.getRequestId(), item.getRequest().getId());

        verify(itemRepository, times(1)).search(anyString(), any(PageRequest.class));
    }

    @Test
    void searchItemEmptyText() {
        when(unionService.checkPageSize(anyInt(), anyInt())).thenReturn(PageRequest.of(5 / 10,10));

        List<ItemDto> itemDtoTest = itemService.searchItem("", 5, 10);

        assertTrue(itemDtoTest.isEmpty());

        verify(itemRepository, times(0)).search(anyString(), any(PageRequest.class));
    }

    @Test
    void addComment() {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.existsById(anyLong())).thenReturn(true);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepository.findFirstByItemIdAndBookerIdAndStatusAndEndBefore(anyLong(), anyLong(), any(Status.class), any(LocalDateTime.class))).thenReturn(Optional.of(firstBooking));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        CommentDto commentDtoTest = itemService.addComment(user.getId(), item.getId(), commentDto);

        assertEquals(commentDtoTest.getId(), comment.getId());
        assertEquals(commentDtoTest.getText(), comment.getText());
        assertEquals(commentDtoTest.getAuthorName(), comment.getAuthor().getName());

        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void addCommentUserNotBookingItem() {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.existsById(anyLong())).thenReturn(true);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepository.findFirstByItemIdAndBookerIdAndStatusAndEndBefore(anyLong(), anyLong(), any(Status.class), any(LocalDateTime.class))).thenReturn(Optional.empty());

        assertThrows(ValidationException.class, () -> itemService.addComment(user.getId(), item.getId(), commentDto));
    }
}
