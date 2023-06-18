package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.User;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemDto addItem(long userId, ItemDto itemDto) {

        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(User.class, "User id " + userId + " not found.");
        }

        User user = userRepository.findById(userId).get();

        Item item = ItemMapper.returnItem(itemDto, user);

        itemRepository.save(item);

        return ItemMapper.returnItemDto(item);
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, long itemId, long userId) {

        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(User.class, "User id " + userId + " not found.");
        }
        User user = userRepository.findById(userId).get();


        if (!itemRepository.existsById(itemId)) {
            throw new NotFoundException(Item.class, "Item id " + userId + " not found.");
        }
        Item item = ItemMapper.returnItem(itemDto, user);

        item.setId(itemId);

        if (!itemRepository.findByOwnerId(userId).contains(item)) {
            throw new NotFoundException(Item.class, "the item was not found with the user id " + userId);
        }

        Item newItem = itemRepository.findById(item.getId()).get();

        if (item.getName() != null) {
            newItem.setName(item.getName());
        }

        if (item.getDescription() != null) {
            newItem.setDescription(item.getDescription());
        }

        if (item.getAvailable() != null) {
            newItem.setAvailable(item.getAvailable());
        }

        itemRepository.save(newItem);

        return ItemMapper.returnItemDto(newItem);
    }

    @Override
    public ItemDto getItemById(long userId) {

        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(User.class, "User id " + userId + " not found.");
        }

        return ItemMapper.returnItemDto(itemRepository.findById(userId).get());
    }

    @Override
    public List<ItemDto> getItemsUser(long userId) {

        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(User.class, "User id " + userId + " not found.");
        }

        return ItemMapper.returnItemDtoList(itemRepository.findByOwnerId(userId));
    }

    @Override
    public  List<ItemDto> searchItem(String text) {

        if (text.equals("")) {
            return Collections.emptyList();
        } else {
            return ItemMapper.returnItemDtoList(itemRepository.search(text));
        }
    }
}