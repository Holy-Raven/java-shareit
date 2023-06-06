package ru.practicum.shareit.item;

import lombok.Getter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Component
public class ItemRepositoryImpl implements ItemRepository {

    static long newId = 1;
    private final HashMap<Long, List<Item>> items = new HashMap<>();

    @Getter
    private final HashMap<Long, Item> allItems = new HashMap<>();

    @Override
    public Item get(long itemId) {
        if (allItems.containsKey(itemId)) {
            return allItems.get(itemId);
        } else {
            throw new NotFoundException("вещи нет в базе");
        }
    }

    @Override
    public List<Item> getAll() {
        return new ArrayList<>(allItems.values());
    }

    @Override
    public Item add(Item item) {

        if (item.getId() == 0) {
            item.setId(newId++);
        }

        allItems.put(item.getId(), item);

        items.compute(item.getOwner(), (userId, userItems) -> {
            if(userItems == null) {
                userItems = new ArrayList<>();
            }
            userItems.add(item);
            return userItems;
        });

        return item;
    }

        @Override
        public Item update(long userId, Item item) {

        Item newItem = allItems.get(item.getId());

        if (item.getName() != null) {
            newItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            newItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            newItem.setAvailable(item.getAvailable());
        }

        List<Item> list = items.get(userId);
        list.remove(item);
        list.add(newItem);

        allItems.put(item.getId(), newItem);

        return newItem;
    }



    @Override
    public List<Item> getItemListByUserId(long userId) {
       return items.getOrDefault(userId, Collections.emptyList());
    }


//    @Override
//    public void deleteByUserIdAndItemId(long userId, long itemId) {
//        if(items.containsKey(userId)) {
//            List<Item> userItems = items.get(userId);
//            userItems.removeIf(item -> item.getId().equals(itemId));
//        }
//    }

}
