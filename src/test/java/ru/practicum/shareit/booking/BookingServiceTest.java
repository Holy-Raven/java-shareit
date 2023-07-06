package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.util.UnionService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BookingServiceTest {

    @Autowired
    private BookingService bookingService;

    @MockBean
    private UnionService unionService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ItemRepository itemRepository;

    @MockBean
    private BookingRepository bookingRepository;

    private User firstUser;

    private User secondUser;

    private Item item;

    private ItemDto itemDto;

    private Booking firstBooking;

    private Booking secondBooking;

    private BookingDto bookingDto;

    @BeforeEach
    void beforeEach() {
        firstUser = User.builder()
                .id(1L)
                .name("Anna")
                .email("anna@yandex.ru")
                .build();

        secondUser = User.builder()
                .id(2L)
                .name("Tiana")
                .email("tiana@yandex.ru")
                .build();

        item = Item.builder()
                .id(1L)
                .name("screwdriver")
                .description("works well, does not ask to eat")
                .available(true)
                .owner(firstUser)
                .build();

        itemDto = ItemMapper.returnItemDto(item);

        firstBooking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now())
                .item(item)
                .booker(firstUser)
                .status(Status.APPROVED)
                .build();

        secondBooking = Booking.builder()
                .id(2L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now())
                .item(item)
                .booker(firstUser)
                .status(Status.WAITING)
                .build();

        bookingDto = BookingDto.builder()
                .itemId(1L)
                .start(LocalDateTime.of(2023, 7, 5, 0, 0))
                .end(LocalDateTime.of(2023, 10, 12, 0, 0))
                .status(Status.APPROVED)
                .build();
    }

    @Test
    void  addBooking() {
        when(itemRepository.existsById(anyLong())).thenReturn(true);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(secondUser));
        when(bookingRepository.save(any(Booking.class))).thenReturn(firstBooking);

        BookingOutDto BookingOutDtoTest = bookingService.addBooking(bookingDto, anyLong());

        assertEquals(BookingOutDtoTest.getItem(), itemDto);
        assertEquals(BookingOutDtoTest.getStatus(), firstBooking.getStatus());
        assertEquals(BookingOutDtoTest.getBooker(), UserMapper.returnUserDto(secondUser));

        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void  addBookingWrongOwner() {
        when(itemRepository.existsById(anyLong())).thenReturn(true);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(firstUser));

        assertThrows(NotFoundException.class, () -> bookingService.addBooking(bookingDto, anyLong()));
    }

    @Test
    void  addBookingItemBooked() {

        item.setAvailable(false);

        when(itemRepository.existsById(anyLong())).thenReturn(true);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(secondUser));

        assertThrows(ValidationException.class, () -> bookingService.addBooking(bookingDto, anyLong()));
    }

    @Test
    void  addBookingNotValidEnd() {
        when(itemRepository.existsById(anyLong())).thenReturn(true);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(secondUser));

        bookingDto.setEnd(LocalDateTime.of(2022, 10, 12, 0, 0));

        assertThrows(ValidationException.class, () -> bookingService.addBooking(bookingDto, anyLong()));
    }

    @Test
    void  addBookingNotValidStart() {
        when(itemRepository.existsById(anyLong())).thenReturn(true);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(secondUser));

        bookingDto.setStart(LocalDateTime.of(2023, 10, 12, 0, 0));

        assertThrows(ValidationException.class, () -> bookingService.addBooking(bookingDto, anyLong()));
    }

    @Test
    void  approveBooking() {
        BookingOutDto bookingOutDtoTest;

        when(bookingRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(secondBooking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(secondBooking);

        bookingOutDtoTest = bookingService.approveBooking(firstUser.getId(), item.getId(), true);
        assertEquals(bookingOutDtoTest.getStatus(), Status.APPROVED);

        bookingOutDtoTest = bookingService.approveBooking(firstUser.getId(), item.getId(), false);
        assertEquals(bookingOutDtoTest.getStatus(), Status.REJECTED);

        verify(bookingRepository, times(2)).save(any(Booking.class));
    }

    @Test
    void  approveBookingWrongUser() {
        when(bookingRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(secondBooking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(secondBooking);

        assertThrows(NotFoundException.class, () -> bookingService.approveBooking(secondUser.getId(), item.getId(), true));
    }

    @Test
    void  approveBookingWrongStatus() {
        when(bookingRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(firstBooking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(firstBooking);

        assertThrows(ValidationException.class, () -> bookingService.approveBooking(firstUser.getId(), item.getId(), true));
    }

    @Test
    void  getBookingById() {
        when(bookingRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(firstBooking));
        when(userRepository.existsById(anyLong())).thenReturn(true);

        BookingOutDto BookingOutDtoTest = bookingService.getBookingById(firstUser.getId(), firstBooking.getId());

        assertEquals(BookingOutDtoTest.getItem(), itemDto);
        assertEquals(BookingOutDtoTest.getStatus(), firstBooking.getStatus());
        assertEquals(BookingOutDtoTest.getBooker(), UserMapper.returnUserDto(firstUser));

    }

    @Test
    void  getBookingByIdError() {
        when(bookingRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(firstBooking));
        when(userRepository.existsById(anyLong())).thenReturn(true);

        assertThrows(NotFoundException.class, () -> bookingService.getBookingById(2L, firstBooking.getId()));
    }

    @Test
    void getAllBookingsByBookerId() {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(unionService.checkPageSize(anyInt(), anyInt())).thenReturn(PageRequest.of(5 / 10,10));
        when(bookingRepository.findAllByBookerIdOrderByStartDesc(anyLong(), any(PageRequest.class))).thenReturn(new PageImpl<>(List.of(firstBooking)));

        String state = "ALL";

        List<BookingOutDto> BookingOutDtoTest = bookingService. getAllBookingsByBookerId(firstUser.getId(), state, 5, 10);

        assertEquals(BookingOutDtoTest.get(0).getId(), firstBooking.getId());
        assertEquals(BookingOutDtoTest.get(0).getStatus(), firstBooking.getStatus());
        assertEquals(BookingOutDtoTest.get(0).getBooker(), UserMapper.returnUserDto(firstUser));

        when(bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartAsc(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class), any(PageRequest.class))).thenReturn(new PageImpl<>(List.of(firstBooking)));
        state = "CURRENT";

        BookingOutDtoTest = bookingService. getAllBookingsByBookerId(firstUser.getId(), state, 5, 10);

        assertEquals(BookingOutDtoTest.get(0).getId(), firstBooking.getId());
        assertEquals(BookingOutDtoTest.get(0).getStatus(), firstBooking.getStatus());
        assertEquals(BookingOutDtoTest.get(0).getBooker(), UserMapper.returnUserDto(firstUser));

        when(bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(anyLong(), any(LocalDateTime.class), any(PageRequest.class))).thenReturn(new PageImpl<>(List.of(firstBooking)));
        state = "PAST";

        BookingOutDtoTest = bookingService. getAllBookingsByBookerId(firstUser.getId(), state, 5, 10);

        assertEquals(BookingOutDtoTest.get(0).getId(), firstBooking.getId());
        assertEquals(BookingOutDtoTest.get(0).getStatus(), firstBooking.getStatus());
        assertEquals(BookingOutDtoTest.get(0).getBooker(), UserMapper.returnUserDto(firstUser));

        when(bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(anyLong(), any(LocalDateTime.class), any(PageRequest.class))).thenReturn(new PageImpl<>(List.of(firstBooking)));
        state = "FUTURE";

        BookingOutDtoTest = bookingService. getAllBookingsByBookerId(firstUser.getId(), state, 5, 10);

        assertEquals(BookingOutDtoTest.get(0).getId(), firstBooking.getId());
        assertEquals(BookingOutDtoTest.get(0).getStatus(), firstBooking.getStatus());
        assertEquals(BookingOutDtoTest.get(0).getBooker(), UserMapper.returnUserDto(firstUser));

        when(bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(anyLong(), any(Status.class), any(PageRequest.class))).thenReturn(new PageImpl<>(List.of(firstBooking)));
        state = "WAITING";

        BookingOutDtoTest = bookingService. getAllBookingsByBookerId(firstUser.getId(), state, 5, 10);

        assertEquals(BookingOutDtoTest.get(0).getId(), firstBooking.getId());
        assertEquals(BookingOutDtoTest.get(0).getStatus(), firstBooking.getStatus());
        assertEquals(BookingOutDtoTest.get(0).getBooker(), UserMapper.returnUserDto(firstUser));

        when(bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(anyLong(), any(Status.class), any(PageRequest.class))).thenReturn(new PageImpl<>(List.of(firstBooking)));
        state = "REJECTED";

        BookingOutDtoTest = bookingService. getAllBookingsByBookerId(firstUser.getId(), state, 5, 10);

        assertEquals(BookingOutDtoTest.get(0).getId(), firstBooking.getId());
        assertEquals(BookingOutDtoTest.get(0).getStatus(), firstBooking.getStatus());
        assertEquals(BookingOutDtoTest.get(0).getBooker(), UserMapper.returnUserDto(firstUser));
    }

    @Test
    void getAllBookingsForAllItemsByOwnerId() {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(itemRepository.findByOwnerId(anyLong())).thenReturn(List.of(item));
        when(unionService.checkPageSize(anyInt(), anyInt())).thenReturn(PageRequest.of(5 / 10,10));
        when(bookingRepository.findAllByItemOwnerIdOrderByStartDesc(anyLong(), any(PageRequest.class))).thenReturn(new PageImpl<>(List.of(firstBooking)));

        String state = "ALL";

        List<BookingOutDto> BookingOutDtoTest = bookingService.getAllBookingsForAllItemsByOwnerId(firstUser.getId(), state, 5, 10);

        assertEquals(BookingOutDtoTest.get(0).getId(), firstBooking.getId());
        assertEquals(BookingOutDtoTest.get(0).getStatus(), firstBooking.getStatus());
        assertEquals(BookingOutDtoTest.get(0).getBooker(), UserMapper.returnUserDto(firstUser));

        when(bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartAsc(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class), any(PageRequest.class))).thenReturn(new PageImpl<>(List.of(firstBooking)));
        state = "CURRENT";

        BookingOutDtoTest = bookingService.getAllBookingsForAllItemsByOwnerId(firstUser.getId(), state, 5, 10);

        assertEquals(BookingOutDtoTest.get(0).getId(), firstBooking.getId());
        assertEquals(BookingOutDtoTest.get(0).getStatus(), firstBooking.getStatus());
        assertEquals(BookingOutDtoTest.get(0).getBooker(), UserMapper.returnUserDto(firstUser));

        when(bookingRepository.findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(anyLong(), any(LocalDateTime.class), any(PageRequest.class))).thenReturn(new PageImpl<>(List.of(firstBooking)));
        state = "PAST";

        BookingOutDtoTest = bookingService.getAllBookingsForAllItemsByOwnerId(firstUser.getId(), state, 5, 10);

        assertEquals(BookingOutDtoTest.get(0).getId(), firstBooking.getId());
        assertEquals(BookingOutDtoTest.get(0).getStatus(), firstBooking.getStatus());
        assertEquals(BookingOutDtoTest.get(0).getBooker(), UserMapper.returnUserDto(firstUser));

        when(bookingRepository.findAllByItemOwnerIdAndStartAfterOrderByStartDesc(anyLong(), any(LocalDateTime.class), any(PageRequest.class))).thenReturn(new PageImpl<>(List.of(firstBooking)));
        state = "FUTURE";

        BookingOutDtoTest = bookingService.getAllBookingsForAllItemsByOwnerId(firstUser.getId(), state, 5, 10);

        assertEquals(BookingOutDtoTest.get(0).getId(), firstBooking.getId());
        assertEquals(BookingOutDtoTest.get(0).getStatus(), firstBooking.getStatus());
        assertEquals(BookingOutDtoTest.get(0).getBooker(), UserMapper.returnUserDto(firstUser));

        when(bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(anyLong(), any(Status.class), any(PageRequest.class))).thenReturn(new PageImpl<>(List.of(firstBooking)));
        state = "WAITING";

        BookingOutDtoTest = bookingService.getAllBookingsForAllItemsByOwnerId(firstUser.getId(), state, 5, 10);

        assertEquals(BookingOutDtoTest.get(0).getId(), firstBooking.getId());
        assertEquals(BookingOutDtoTest.get(0).getStatus(), firstBooking.getStatus());
        assertEquals(BookingOutDtoTest.get(0).getBooker(), UserMapper.returnUserDto(firstUser));

        when(bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(anyLong(), any(Status.class), any(PageRequest.class))).thenReturn(new PageImpl<>(List.of(firstBooking)));
        state = "REJECTED";

        BookingOutDtoTest = bookingService.getAllBookingsForAllItemsByOwnerId(firstUser.getId(), state, 5, 10);

        assertEquals(BookingOutDtoTest.get(0).getId(), firstBooking.getId());
        assertEquals(BookingOutDtoTest.get(0).getStatus(), firstBooking.getStatus());
        assertEquals(BookingOutDtoTest.get(0).getBooker(), UserMapper.returnUserDto(firstUser));
    }

    @Test
    void getAllBookingsForAllItemsByOwnerIdNotHaveItems () {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(itemRepository.findByOwnerId(anyLong())).thenReturn(List.of());

        assertThrows(ValidationException.class, () -> bookingService.getAllBookingsForAllItemsByOwnerId(firstUser.getId(), "APPROVED", 5, 10));
    }
}
