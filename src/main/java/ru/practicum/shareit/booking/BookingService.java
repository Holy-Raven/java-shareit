package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;

public interface BookingService {

    BookingOutDto addBooking(BookingDto bookingDto, Long booker);

    BookingOutDto approveBooking(Long userId, Long bookingId, Boolean approved);

    BookingOutDto getBookingById(Long bookingId, Long userId);

}
