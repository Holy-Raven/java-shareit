package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
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

        Booking booking = BookingMapper.returnBooking(bookingDto);
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

        return BookingMapper.returnBookingDto(booking);
    }

    @Override
    public BookingOutDto approveBooking(Long userId, Long bookingId, Boolean approved ) {

        if (!bookingRepository.existsById(bookingId)) {
            throw new NotFoundException(Booking.class, "Booking id " + bookingId + " not found.");
        }
        Booking booking = bookingRepository.findById(bookingId).get();

        if (booking.getItem().getOwner().getId() != userId) {
            throw new ValidationException("Only owner items can change booking status");
        }

        if (approved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }

        return BookingMapper.returnBookingDto(booking);
    }
}
