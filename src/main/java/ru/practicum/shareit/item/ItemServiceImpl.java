package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository repository;
    private final UserRepository userRepository;

//    @Override
//    public List<Item> getItems(long userId) {
//        return repository.findByUserId(userId);
//    }

    @Override
    public Item addNewItem(long owner, Item item) {

        userRepository.get(owner);

        item.setOwner(owner);

        return repository.add(item);
    }

//    @Override
//    public void deleteItem(long userId, long itemId) {
//        repository.deleteByUserIdAndItemId(userId, itemId);
//    }
}