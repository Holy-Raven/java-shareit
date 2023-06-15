package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public Item addItem(long userId, Item item) {

        userRepository.get(userId);
        item.setOwner(userId);
        return itemRepository.add(item);
    }

    @Override
    public Item updateItem(Item item, long itemId, long userId) {

        userRepository.get(userId);
        itemRepository.get(itemId);

        item.setId(itemId);
        item.setOwner(userId);

        if (!getItemsUser(userId).contains(item)) {
            throw new NotFoundException(Item.class, "the item was not found with the user id " + userId);
        }

        return itemRepository.update(userId, item);
    }

    @Override
    public Item getItemById(long userId) {
        return itemRepository.get(userId);
    }


    @Override
    public List<Item> getItemsUser(long userId) {

        userRepository.get(userId);
        return itemRepository.getItemListByUserId(userId);
    }

    @Override
    public  List<Item> searchItem(String text) {

        return itemRepository.search(text);
     }
}