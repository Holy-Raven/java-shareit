package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Component
public class ItemRepositoryImpl implements ItemRepository {

    static long newId = 1;
    private final Map<Long, List<Item>> items = new HashMap<>();

//    @Override
//    public List<Item> findByUserId(long userId) {
//        return items.getOrDefault(userId, Collections.emptyList());
//    }

    @Override
    public Item add(Item item) {


        if (item.getId() == 0) {
            item.setId(newId++);
        }


        items.compute(item.getOwner(), (userId, userItems) -> {

            if(userItems == null) {
                userItems = new ArrayList<>();
            }

            userItems.add(item);
            return userItems;
        });

        return item;
    }

//    @Override
//    public void deleteByUserIdAndItemId(long userId, long itemId) {
//        if(items.containsKey(userId)) {
//            List<Item> userItems = items.get(userId);
//            userItems.removeIf(item -> item.getId().equals(itemId));
//        }
//    }

}
