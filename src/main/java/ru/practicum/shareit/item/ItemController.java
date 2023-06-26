package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import java.util.List;

import static ru.practicum.shareit.util.Constant.HEADER_USER;

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

    @PostMapping
    public ItemDto addItem(@RequestHeader(HEADER_USER) Long userId,
                           @RequestBody @Valid ItemDto itemDto) {

        log.info("User {}, add new item {}", userId, itemDto.getName());
        return itemService.addItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader(HEADER_USER) Long userId,
                              @RequestBody ItemDto itemDto,
                              @PathVariable Long itemId) {

        log.info("User {}, update item {}", userId, itemDto.getName());
        return itemService.updateItem(itemDto, itemId, userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItem(@RequestHeader(HEADER_USER) Long userId,
                           @PathVariable Long itemId) {
        log.info("Get item {}", itemId);
        return itemService.getItemById(itemId, userId);
    }

    @GetMapping
    public List<ItemDto> getAllItemsUser(@RequestHeader(HEADER_USER) Long userId) {

        log.info("List items User {}", userId);
        return itemService.getItemsUser(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> getSearchItem(String text) {

        log.info("Get item with key substring {}", text);
        return itemService.searchItem(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader(HEADER_USER) Long userId,
                                 @PathVariable Long itemId,
                                 @RequestBody @Valid CommentDto commentDto) {

        log.info("User {} add comment for Item {}", userId, itemId);
        return itemService.addComment(userId, itemId, commentDto);
    }
}