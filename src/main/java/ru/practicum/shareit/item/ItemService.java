package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

interface ItemService {

//    List<Item> getItems(long userId);

    Item addNewItem(long owner, Item item);

//    void deleteItem(long userId, long itemId);
}