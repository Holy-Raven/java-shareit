package ru.practicum.shareit.item;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

@Transactional(readOnly = true)
interface ItemService {

    @Transactional
    ItemDto addItem(long owner, ItemDto itemDto);

    @Transactional
    ItemDto updateItem(ItemDto itemDto, long itemId, long userId);

    @Transactional(readOnly = true)
    ItemDto getItemById(long itemId);

    @Transactional(readOnly = true)
    List<ItemDto> getItemsUser(long userId);

    @Transactional(readOnly = true)
    List<ItemDto> searchItem(String text);

}
