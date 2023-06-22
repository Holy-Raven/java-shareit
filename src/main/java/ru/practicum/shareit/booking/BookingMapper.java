package ru.practicum.shareit.booking;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.user.UserMapper;

import java.util.ArrayList;
import java.util.List;

@Component
public class BookingMapper {

    public static BookingOutDto returnBookingDto(Booking booking) {
        BookingOutDto bookingOutDto = BookingOutDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .item(ItemMapper.returnItemDto(booking.getItem()))
                .booker(UserMapper.returnUserDto(booking.getBooker()))
                .build();
        return bookingOutDto;
    }
    public static BookingShortDto returnBookingShortDto(Booking booking) {
        BookingShortDto bookingShortDto = BookingShortDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .bookerId(booking.getBooker().getId())
                .build();
        return bookingShortDto;
    }

    public static Booking returnBooking(BookingDto bookingDto) {

        Booking booking = Booking.builder()
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .build();

        if (bookingDto.getStatus() == null) {
            booking.setStatus(Status.WAITING);
        } else {
            booking.setStatus(bookingDto.getStatus());
        }
        return booking;
    }

    public static List<BookingOutDto> returnBookingDtoList(Iterable<Booking> bookings) {
        List<BookingOutDto> result = new ArrayList<>();

        for (Booking booking : bookings) {
            result.add(returnBookingDto(booking));
        }
        return result;
    }
}
