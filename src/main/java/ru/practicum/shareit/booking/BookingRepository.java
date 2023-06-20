package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerIdOrderByStartDesc(long booker_id);

    List<Booking> findAllByBookerIdAndStartOrderByStartDesc(long booker_id, LocalDateTime dateTime);

    List<Booking> findAllByBookerIdAndStartBeforeOrderByStartDesc(long booker_id, LocalDateTime dateTime);

    List<Booking> findAllByBookerIdAndStartAfterOrderByStartDesc(long booker_id, LocalDateTime dateTime);

    List<Booking> findAllByBookerIdAndStatusOrderByStartDesc(long booker_id, Status status);

    List<Booking> findAllByItemOwnerIdOrderByStartDesc(long owner_id);

    List<Booking> findAllByItemOwnerIdAndStartOrderByStartDesc(long owner_id, LocalDateTime dateTime);

    List<Booking> findAllByItemOwnerIdAndStartBeforeOrderByStartDesc(long owner_id, LocalDateTime dateTime);

    List<Booking> findAllByItemOwnerIdAndStartAfterOrderByStartDesc(long owner_id, LocalDateTime dateTime);

    List<Booking> findAllByItemOwnerIdAndStatusOrderByStartDesc(long owner_id, Status status);

    Optional<Booking> findFirstByItemIdAndStatusAndStartAfterOrderByStartAsc(long item_Id, Status status, LocalDateTime dateTime);

    Optional<Booking> findFirstByItemIdAndStatusAndStartBeforeOrderByStartDesc(long item_Id, Status status, LocalDateTime dateTime);

}
