package ru.practicum.shareit.booking;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.List;

@Component
public class BookingMapper {


    public BookingDto returnBookingDto(Booking booking) {
        BookingDto bookingDto = BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .build();
        return bookingDto;
    }

    public Booking returnBooking(BookingDto bookingDto, Item item, User user) {
        Booking booking = Booking.builder()
                .id(bookingDto.getId())
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .status(bookingDto.getStatus())
                .item(item)
                .booker(user)
                .build();
        return booking;
    }

    public List<BookingDto> returnBokingDtoList(Iterable<Booking> bookings) {
        List<BookingDto> result = new ArrayList<>();

        for (Booking booking : bookings) {
            result.add(returnBookingDto(booking));
        }

        return result;
    }
}
