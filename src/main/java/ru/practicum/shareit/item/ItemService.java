package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

interface ItemService {

    ItemDto addItem(long owner, ItemDto itemDto);

    ItemDto updateItem(ItemDto itemDto, long itemId, long userId);

    ItemDto getItemById(long itemId);

    List<ItemDto> getItemsUser(long userId);

    List<ItemDto> searchItem(String text);

}
