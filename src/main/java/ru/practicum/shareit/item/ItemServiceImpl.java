package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemMapper mapper;

    @Override
    public ItemDto addItem(long userId, ItemDto itemDto) {

        Item item = mapper.returnItem(itemDto);

        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException(User.class, "User id " + userId + " not found.");
        }

        item.setOwner(userId);
        itemRepository.add(item);

        return mapper.returnItemDto(item);
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, long itemId, long userId) {

        Item item = mapper.returnItem(itemDto);

        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException(User.class, "User id " + userId + " not found.");
        }

        itemRepository.get(itemId);

        item.setId(itemId);
        item.setOwner(userId);

        if (!itemRepository.getItemListByUserId(userId).contains(item)) {
            throw new NotFoundException(Item.class, "the item was not found with the user id " + userId);
        }

        Item newItem = itemRepository.get(item.getId());

        if (item.getName() != null) {
            newItem.setName(item.getName());
        }

        if (item.getDescription() != null) {
            newItem.setDescription(item.getDescription());
        }

        if (item.getAvailable() != null) {
            newItem.setAvailable(item.getAvailable());
        }

        itemRepository.update(userId, newItem);

        return mapper.returnItemDto(newItem);
    }

    @Override
    public ItemDto getItemById(long userId) {
        return mapper.returnItemDto(itemRepository.get(userId));
    }


    @Override
    public List<ItemDto> getItemsUser(long userId) {

        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException(User.class, "User id " + userId + " not found.");
        }

        return mapper.returnItemDtoList(itemRepository.getItemListByUserId(userId));
    }

    @Override
    public  List<ItemDto> searchItem(String text) {

        if (text.equals("")) {
            return Collections.emptyList();
        } else {
            return mapper.returnItemDtoList(itemRepository.search(text));
        }
    }
}