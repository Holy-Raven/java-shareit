package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * TODO Sprint add-controllers.
 */

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;
    private final ItemMapper mapper;

    @PostMapping
    public ItemDto add(@RequestHeader("X-Sharer-User-Id") long userId,
                       @RequestBody @Valid ItemDto itemDto) {

        Item item = mapper.returnItem(itemDto);
        itemService.addItem(userId, item);
        return mapper.returnItemDto(item);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") long userId,
                              @RequestBody ItemDto itemDto,
                              @PathVariable Long itemId) {

        Item item = mapper.returnItem(itemDto);
        return mapper.returnItemDto(itemService.updateItem(item, itemId, userId));

    }

    @GetMapping("/{itemId}")
    public ItemDto getUser(@PathVariable Long itemId) {

        return mapper.returnItemDto(itemService.getItemById(itemId));
    }

    @GetMapping
    public List<ItemDto> getAllItemsUser(@RequestHeader("X-Sharer-User-Id") long userId) {

        return itemService.getItemsUser(userId)
                .stream()
                .map(mapper::returnItemDto)
                .collect(toList());
    }

    @GetMapping("/search")
    public List<ItemDto> getSearchItem(String text) {

        return itemService.searchItem(text)
                .stream()
                .map(mapper::returnItemDto)
                .collect(toList());
    }
}