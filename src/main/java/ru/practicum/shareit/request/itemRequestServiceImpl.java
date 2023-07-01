package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.util.UnionService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.exception.ValidationException;

import java.util.Collections;
import java.util.List;
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class itemRequestServiceImpl implements ItemRequestService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final UnionService unionService;
    private final ItemRequestRepository itemRequestRepository;


    @Transactional
    @Override
    public ItemRequestDto addRequest(ItemRequestDto itemRequestDto, long userId) {

        unionService.checkUser(userId);

        User user = userRepository.findById(userId).get();

        ItemRequest itemRequest = ItemRequestMapper.returnItemRequest(itemRequestDto, user);

        itemRequestRepository.save(itemRequest);

        return ItemRequestMapper.returnItemRequestDto(itemRequest);
    }

    @Override
    public List<ItemRequestDto> getRequests(long userId) {

        unionService.checkUser(userId);

        List<ItemRequest> requestList = itemRequestRepository.findByRequesterIdOrderByCreatedAsc(userId);

        return ItemRequestMapper.returnItemRequestDtoList(requestList);
    }

    @Override
    public List<ItemRequestDto> getAllRequests(Integer from, Integer size) {

        if (from == 0 && size == null) {
            return Collections.emptyList();
        }

        if (size <= 0) {
            throw new ValidationException("\"size\" must be greater than 0");
        }

        if (from < 0) {
            throw new ValidationException("\"from\" must be greater than or equal to 0");
        }

        PageRequest pageRequest = PageRequest.of(from, size, Sort.Direction.DESC, "created");

        Iterable<ItemRequest> itemRequests = itemRequestRepository.findAll(pageRequest);

        return ItemRequestMapper.returnItemRequestDtoList(itemRequests);
    }

    @Override
    public ItemRequestDto getRequestById(long userId, long requestId) {

        unionService.checkUser(userId);
        unionService.checkRequest(requestId);

        ItemRequest itemRequest = itemRequestRepository.findById(requestId).get();

        if (itemRequest.getRequester().getId() != userId) {
            throw new ValidationException("this request does not belong to the user");
        }

        return ItemRequestMapper.returnItemRequestDto(itemRequest);
    }
}
