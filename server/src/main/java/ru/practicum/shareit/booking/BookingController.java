package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;

import java.util.List;

import static ru.practicum.shareit.util.Constant.HEADER_USER;

/**
 * TODO Sprint add-bookings.
 */

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingOutDto> addBooking(@RequestHeader(HEADER_USER) Long userId,
                                                    @RequestBody BookingDto bookingDto) {

        log.info("User {}, add new booking", userId);
        return ResponseEntity.ok(bookingService.addBooking(bookingDto, userId));
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<BookingOutDto> approveBooking(@RequestHeader(HEADER_USER) Long userId,
                                                        @PathVariable Long bookingId,
                                                        @RequestParam Boolean approved) {

        log.info("User {}, changed the status booking {}", userId, bookingId);
        return ResponseEntity.ok(bookingService.approveBooking(userId, bookingId, approved));
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingOutDto> getBookingById(@RequestHeader(HEADER_USER) Long userId,
                                                        @PathVariable Long bookingId) {

        log.info("Get booking {}", bookingId);
        return ResponseEntity.ok(bookingService.getBookingById(userId, bookingId));
    }

    @GetMapping
    public ResponseEntity<List<BookingOutDto>> getAllBookingsByBookerId(@RequestHeader(HEADER_USER) Long userId,
                                                                        @RequestParam(defaultValue = "ALL", required = false) String state,
                                                                        @RequestParam(defaultValue = "0", required = false) Integer from,
                                                                        @RequestParam(defaultValue = "10", required = false) Integer size) {

        log.info("Get all bookings by booker Id {}", userId);
        return ResponseEntity.ok(bookingService.getAllBookingsByBookerId(userId, state, from, size));
    }

    @GetMapping("/owner")
    public ResponseEntity<List<BookingOutDto>> getAllBookingsForAllItemsByOwnerId(@RequestHeader(HEADER_USER) Long userId,
                                                                                  @RequestParam(defaultValue = "ALL", required = false) String state,
                                                                                  @RequestParam(defaultValue = "0", required = false) Integer from,
                                                                                  @RequestParam(defaultValue = "10", required = false) Integer size) {

        log.info("Get all bookings for all items by owner Id {}", userId);
        return ResponseEntity.ok(bookingService.getAllBookingsForAllItemsByOwnerId(userId, state, from, size));
    }
}
