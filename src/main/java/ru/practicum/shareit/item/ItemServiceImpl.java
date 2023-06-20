package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    @Override
    public ItemDto addItem(long userId, ItemDto itemDto) {

        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(User.class, "User id " + userId + " not found.");
        }

        User user = userRepository.findById(userId).get();

        Item item = ItemMapper.returnItem(itemDto, user);

        itemRepository.save(item);

        return ItemMapper.returnItemDto(item);
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, long itemId, long userId) {

        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(User.class, "User id " + userId + " not found.");
        }
        User user = userRepository.findById(userId).get();


        if (!itemRepository.existsById(itemId)) {
            throw new NotFoundException(Item.class, "Item id " + userId + " not found.");
        }
        Item item = ItemMapper.returnItem(itemDto, user);

        item.setId(itemId);

        if (!itemRepository.findByOwnerId(userId).contains(item)) {
            throw new NotFoundException(Item.class, "the item was not found with the user id " + userId);
        }

        Item newItem = itemRepository.findById(item.getId()).get();

        if (item.getName() != null) {
            newItem.setName(item.getName());
        }

        if (item.getDescription() != null) {
            newItem.setDescription(item.getDescription());
        }

        if (item.getAvailable() != null) {
            newItem.setAvailable(item.getAvailable());
        }

        itemRepository.save(newItem);

        return ItemMapper.returnItemDto(newItem);
    }


    @Override
    public ItemDto getItemById(long itemId, long userId) {

        if (!itemRepository.existsById(itemId)) {
            throw new NotFoundException(Item.class, "Item id " + itemId + " not found.");
        }
        Item item = itemRepository.findById(itemId).get();

        ItemDto itemDto = ItemMapper.returnItemDto(item);

        if (userId > 0) {

            if (!userRepository.existsById(userId)) {
                throw new NotFoundException(User.class, "User id " + itemId + " not found.");
            }

//            List<Booking> lastBookings =
//                    bookingRepository.findByItemIdAndBookerIdAndStatusAndStartBeforeOrderByStartDesc(itemId, userId, Status.APPROVED, LocalDateTime.now());
//
//            List<Booking> nextBookings =
//                    bookingRepository.findByItemIdAndBookerIdAndStatusAndStartAfterOrderByStartDesc(itemId, userId, Status.APPROVED, LocalDateTime.now());
//
//            if (!lastBookings.isEmpty()) {
//                itemDto.setLastBooking(BookingMapper.returnBookingDto(lastBookings.get(0)));
//            }
//
//            if (!nextBookings.isEmpty()) {
//                itemDto.setNextBooking(BookingMapper.returnBookingDto(nextBookings.get(0)));
//            }

            Optional<Booking> lastBooking = bookingRepository.findFirstByItemIdAndStatusAndStartBeforeOrderByStartDesc(itemId, Status.APPROVED, LocalDateTime.now());
            Optional<Booking> nextBooking = bookingRepository.findFirstByItemIdAndStatusAndStartAfterOrderByStartAsc(itemId,  Status.APPROVED, LocalDateTime.now());

            if (lastBooking.isPresent()) {
                itemDto.setLastBooking(BookingMapper.returnBookingShortDto(lastBooking.get()));
            } else {
                itemDto.setLastBooking(null);
            }

            if (nextBooking.isPresent()) {
                itemDto.setNextBooking(BookingMapper.returnBookingShortDto(nextBooking.get()));
            } else {
                itemDto.setNextBooking(null);
            }

        }
            return itemDto;
    }

    @Override
    public List<ItemDto> getItemsUser(long userId) {

        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(User.class, "User id " + userId + " not found.");
        }

        return ItemMapper.returnItemDtoList(itemRepository.findByOwnerId(userId));
    }

    @Override
    public  List<ItemDto> searchItem(String text) {

        if (text.equals("")) {
            return Collections.emptyList();
        } else {
            return ItemMapper.returnItemDtoList(itemRepository.search(text));
        }
    }
}