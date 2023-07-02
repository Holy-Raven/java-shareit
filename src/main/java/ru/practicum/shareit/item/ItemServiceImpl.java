package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.util.UnionService;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemRequestRepository itemRequestRepository;
    private final UnionService unionService;

    @Transactional
    @Override
    public ItemDto addItem(long userId, ItemDto itemDto) {

        unionService.checkUser(userId);

        User user = userRepository.findById(userId).get();
        Item item = ItemMapper.returnItem(itemDto, user);

        if (itemDto.getRequestId() != null) {
            unionService.checkRequest(itemDto.getRequestId());
            item.setRequest(itemRequestRepository.findById(itemDto.getRequestId()).get());
        }
        itemRepository.save(item);

        return ItemMapper.returnItemDto(item);
    }

    @Transactional
    @Override
    public ItemDto updateItem(ItemDto itemDto, long itemId, long userId) {

        unionService.checkUser(userId);
        User user = userRepository.findById(userId).get();

        unionService.checkItem(itemId);
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

    @Transactional(readOnly = true)
    @Override
    public ItemDto getItemById(long itemId, long userId) {

        unionService.checkItem(itemId);
        Item item = itemRepository.findById(itemId).get();

        ItemDto itemDto = ItemMapper.returnItemDto(item);

        unionService.checkUser(userId);

        if (item.getOwner().getId() == userId) {

            Optional<Booking> lastBooking = bookingRepository.findFirstByItemIdAndStatusAndStartBeforeOrderByStartDesc(itemId, Status.APPROVED, LocalDateTime.now());
            Optional<Booking> nextBooking = bookingRepository.findFirstByItemIdAndStatusAndStartAfterOrderByStartAsc(itemId, Status.APPROVED, LocalDateTime.now());

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

        List<Comment> commentList = commentRepository.findAllByItemId(itemId);

        if (!commentList.isEmpty()) {
            itemDto.setComments(CommentMapper.returnICommentDtoList(commentList));
        } else {
            itemDto.setComments(Collections.emptyList());
        }

        return itemDto;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ItemDto> getItemsUser(long userId, Integer from, Integer size) {

        unionService.checkUser(userId);
        PageRequest pageRequest = unionService.checkPageSize(from, size);

        List<ItemDto> resultList = new ArrayList<>();

        for (ItemDto itemDto : ItemMapper.returnItemDtoList(itemRepository.findByOwnerId(userId, pageRequest))) {

            Optional<Booking> lastBooking = bookingRepository.findFirstByItemIdAndStatusAndStartBeforeOrderByStartDesc(itemDto.getId(), Status.APPROVED, LocalDateTime.now());
            Optional<Booking> nextBooking = bookingRepository.findFirstByItemIdAndStatusAndStartAfterOrderByStartAsc(itemDto.getId(), Status.APPROVED, LocalDateTime.now());

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

            resultList.add(itemDto);
        }

        for (ItemDto itemDto : resultList) {

            List<Comment> commentList = commentRepository.findAllByItemId(itemDto.getId());

            if (!commentList.isEmpty()) {
                itemDto.setComments(CommentMapper.returnICommentDtoList(commentList));
            } else {
                itemDto.setComments(Collections.emptyList());
            }
        }

        return resultList;
    }

    @Transactional(readOnly = true)
    @Override
    public  List<ItemDto> searchItem(String text, Integer from, Integer size) {

        PageRequest pageRequest = unionService.checkPageSize(from, size);

        if (text.equals("")) {
            return Collections.emptyList();
        } else {
            return ItemMapper.returnItemDtoList(itemRepository.search(text, pageRequest));
        }
    }

    @Transactional
    @Override
    public CommentDto addComment(long userId, long itemId, CommentDto commentDto) {

        unionService.checkUser(userId);
        User user = userRepository.findById(userId).get();

        unionService.checkItem(itemId);
        Item item = itemRepository.findById(itemId).get();

        LocalDateTime dateTime = LocalDateTime.now();

        Optional<Booking> booking = bookingRepository.findFirstByItemIdAndBookerIdAndStatusAndEndBefore(itemId, userId, Status.APPROVED, dateTime);

        if (booking.isEmpty()) {
            throw new ValidationException("User " + userId + " not booking this item " + itemId);
        }

        Comment comment = CommentMapper.returnComment(commentDto, item, user, dateTime);
        commentRepository.save(comment);

        return CommentMapper.returnCommentDto(comment);
    }
}