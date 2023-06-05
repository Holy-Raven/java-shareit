package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

interface ItemRepository {

//    List<Item> findByUserId(long userId);

    Item add(Item item);

//    void deleteByUserIdAndItemId(long userId, long itemId);

}