package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
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

        log.info("User {}, add new item {}", userId, itemDto.getName());
        return itemService.addItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") long userId,
                              @RequestBody ItemDto itemDto,
                              @PathVariable Long itemId) {

        log.info("User {}, update item {}", userId, itemDto.getName());
        return itemService.updateItem(itemDto, itemId, userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getUser(@PathVariable Long itemId) {

        log.info("Get item {}", itemId);
        return itemService.getItemById(itemId);
    }

    @GetMapping
    public List<ItemDto> getAllItemsUser(@RequestHeader("X-Sharer-User-Id") long userId) {

        log.info("List items User {}", userId);
        return itemService.getItemsUser(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> getSearchItem(String text) {

        log.info("Get item with key substring {}", text);
        return itemService.searchItem(text);
    }
}