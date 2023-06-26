package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerIdOrderByStartDesc(long bookerId);

    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartAsc(long bookerId, LocalDateTime start, LocalDateTime end);

    List<Booking> findAllByBookerIdAndEndBeforeOrderByStartDesc(long bookerId, LocalDateTime dateTime);

    List<Booking> findAllByBookerIdAndStartAfterOrderByStartDesc(long bookerId, LocalDateTime dateTime);

    List<Booking> findAllByBookerIdAndStatusOrderByStartDesc(long bookerId, Status status);

    List<Booking> findAllByItemOwnerIdOrderByStartDesc(long ownerId);

    List<Booking> findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartAsc(long ownerId, LocalDateTime start, LocalDateTime end);

    List<Booking> findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(long ownerId, LocalDateTime dateTime);

    List<Booking> findAllByItemOwnerIdAndStartAfterOrderByStartDesc(long ownerId, LocalDateTime dateTime);

    List<Booking> findAllByItemOwnerIdAndStatusOrderByStartDesc(long ownerId, Status status);

    Optional<Booking> findFirstByItemIdAndStatusAndStartAfterOrderByStartAsc(long itemId, Status status, LocalDateTime dateTime);

    Optional<Booking> findFirstByItemIdAndStatusAndStartBeforeOrderByStartDesc(long itemId, Status status, LocalDateTime dateTime);

    Optional<Booking> findFirstByItemIdAndBookerIdAndStatusAndEndBefore(long itemId, long bookerId, Status status, LocalDateTime dateTime);
}
