package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDto addRequest(ItemRequestDto itemRequestDto, long userId);

    List<ItemRequestDto> getRequests(long userId);

    List<ItemRequestDto> getAllRequests(Long userId, Integer from, Integer size);

    ItemRequestDto getRequestById(long userId, long requestId);

    ItemRequestDto addItemsToRequest(ItemRequest itemRequest);
}

