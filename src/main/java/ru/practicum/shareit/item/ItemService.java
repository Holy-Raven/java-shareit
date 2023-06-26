package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

interface ItemService {

    ItemDto addItem(long owner, ItemDto itemDto);

    ItemDto updateItem(ItemDto itemDto, long itemId, long userId);

    ItemDto getItemById(long itemId, long userId);

    List<ItemDto> getItemsUser(long userId);

    List<ItemDto> searchItem(String text);

    CommentDto addComment(long userId, long itemId, CommentDto commentDto);
}
