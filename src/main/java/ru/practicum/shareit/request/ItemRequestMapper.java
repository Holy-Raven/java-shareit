package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.List;

public class ItemRequestMapper {

    public static ItemRequestDto returnItemRequestDto(ItemRequest itemRequest) {
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .build();

        return itemRequestDto;
    }

    public static ItemRequest returnItemRequest(ItemRequestDto itemRequestDto, User user) {
        ItemRequest itemRequest = ItemRequest.builder()
                .id(itemRequestDto.getId())
                .description(itemRequestDto.getDescription())
                .created(itemRequestDto.getCreated())
                .requestor(user)
                .build();
        return itemRequest;
    }

    public static List<ItemRequestDto> returnItemRequestDtoList(Iterable<ItemRequest> requests) {
        List<ItemRequestDto> result = new ArrayList<>();

        for (ItemRequest itemRequest : requests) {
            result.add(returnItemRequestDto(itemRequest));
        }
        return result;
    }
}
