package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;

import java.util.List;

public interface BookingService {

    BookingOutDto addBooking(BookingDto bookingDto, Long userId);

    BookingOutDto approveBooking(Long userId, Long bookingId, Boolean approved);

    BookingOutDto getBookingById(Long userId, Long bookingId);

    List<BookingOutDto> getAllBookingsByBookerId(Long userId, String state);

    List<BookingOutDto> getAllBookingsForAllItemsByOwnerId(Long userId, String state);

}
