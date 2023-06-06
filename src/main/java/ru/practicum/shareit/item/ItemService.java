package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

interface ItemService {

    Item addItem(long owner, Item item);

    Item updateItem(Item item, long itemId, long userId);

    Item getItemById(long itemId);

    List<Item> getItemsUser(long userId);


//    List<Item> getAllItems();
//    void deleteItem(long userId, long itemId);
}