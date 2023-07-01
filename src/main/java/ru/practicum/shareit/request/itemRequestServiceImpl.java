package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.util.UnionService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.exception.ValidationException;

import java.util.ArrayList;
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

        List<ItemRequest> itemRequests = itemRequestRepository.findByRequesterIdOrderByCreatedAsc(userId);

        List<ItemRequestDto> result = new ArrayList<>();
        for (ItemRequest itemRequest : itemRequests) {
            result.add(addItemsToRequest(itemRequest));
        }
        return result;

    }

    @Override
    public List<ItemRequestDto> getAllRequests(Integer from, Integer size, Long userId) {

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

        List<ItemRequest> itemRequests = itemRequestRepository.findByIdIsNotOrderByCreatedAsc(userId, pageRequest);

        List<ItemRequestDto> result = new ArrayList<>();
        for (ItemRequest itemRequest : itemRequests) {
            result.add(addItemsToRequest(itemRequest));
        }
        return result;
    }

    @Override
    public ItemRequestDto getRequestById(long userId, long requestId) {

        unionService.checkUser(userId);
        unionService.checkRequest(requestId);

        ItemRequest itemRequest = itemRequestRepository.findById(requestId).get();

        return addItemsToRequest(itemRequest);
    }

    private ItemRequestDto addItemsToRequest (ItemRequest itemRequest) {

            ItemRequestDto itemRequestDto = ItemRequestMapper.returnItemRequestDto(itemRequest);
            List<Item> items = itemRepository.findByRequestId(itemRequest.getId());
            itemRequestDto.setItems(ItemMapper.returnItemDtoList(items));

        return itemRequestDto;
    }
}
