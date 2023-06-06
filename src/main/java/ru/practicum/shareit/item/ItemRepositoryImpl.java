package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Component
public class ItemRepositoryImpl implements ItemRepository {

    static long newId = 1;

    private final HashMap<Long, List<Item>> items = new HashMap<>();

    private final HashMap<Long, Item> allItems = new HashMap<>();

    @Override
    public Item get(long itemId) {
        if (allItems.containsKey(itemId)) {
            return allItems.get(itemId);
        } else {
            throw new NotFoundException("Item id " + itemId + " not found.");
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

    @Override
    public List<Item> search(String text) {

        Set<Item> set = new HashSet<>();

        if (text.equals("")) {
            return Collections.emptyList();
        } else {
            for (Item item : getAll()) {
                if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                    if (item.getAvailable()) {
                        set.add(item);
                    }
                }
                if (item.getDescription().toLowerCase().contains(text.toLowerCase())) {
                    if (item.getAvailable()) {
                        set.add(item);
                    }
                }
            }
        }

        return new ArrayList<>(set);
    }
}
