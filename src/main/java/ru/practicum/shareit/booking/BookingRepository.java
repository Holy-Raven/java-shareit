package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerIdOrderByStartDesc(long booker_id);

    List<Booking> findAllByBookerIdAndStartOrderByStartDesc(long booker_id, LocalDateTime dateTime);

    List<Booking> findAllByBookerIdAndStartBeforeOrderByStartDesc(long booker_id, LocalDateTime dateTime);

    List<Booking> findAllByBookerIdAndStartAfterOrderByStartDesc(long booker_id, LocalDateTime dateTime);

    List<Booking> findAllByBookerIdAndStatusOrderByStartDesc(long booker_id, Status status);
}
