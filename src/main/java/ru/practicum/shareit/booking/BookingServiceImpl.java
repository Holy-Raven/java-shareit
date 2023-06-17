package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.hibernate.type.LocalDateTimeType;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingMapper mapper;

    @Override
    public BookingOutDto addBooking(BookingDto bookingDto, Long booker) {

        if (!itemRepository.existsById(bookingDto.getItemId())) {
            throw new NotFoundException(Item.class, "Item id " + bookingDto.getItemId() + " not found.");
        }
        Item item = itemRepository.findById(bookingDto.getItemId()).get();

        if (!userRepository.existsById(booker)) {
            throw new NotFoundException(User.class, "User id " + booker + " not found.");
        }
        User user = userRepository.findById(booker).get();

        Booking booking = mapper.returnBooking(bookingDto);
        booking.setItem(item);
        booking.setBooker(user);

        if (item.getOwner().equals(user)) {
            throw new ValidationException("Owner can't book his item");
        }
        if (!item.getAvailable()) {
            throw new ValidationException("Item is booked");
        }
        if (booking.getStart().isAfter(booking.getEnd())) {
            throw new ValidationException("Start cannot be later than end");
        }
        if (booking.getStart().isEqual(booking.getEnd())) {
            throw new ValidationException("Start cannot be equal than end");
        }

        bookingRepository.save(booking);

        return mapper.returnBookingDto(booking);
    }
}
