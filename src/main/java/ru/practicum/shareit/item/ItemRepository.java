package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

interface ItemRepository {

    Item get(long itemId);

    List<Item> getAll();

    Item add(Item item);

    Item update(long userId, Item item);

    List<Item> getItemListByUserId(long userId);

    List<Item> search(String text);

}