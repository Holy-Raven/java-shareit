package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import static ru.practicum.shareit.util.Constant.HEADER_USER;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> addItem(@RequestHeader(HEADER_USER) Long userId,
                                          @RequestBody @Valid ItemDto itemDto) {

        log.info("User {}, add new item {}", userId, itemDto.getName());
        return itemClient.addItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader(HEADER_USER) Long userId,
                                             @RequestBody ItemDto itemDto,
                                             @PathVariable("itemId") Long itemId) {

        log.info("User {}, update item {}", userId, itemDto.getName());
        return itemClient.updateItem(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@RequestHeader(HEADER_USER) Long userId,
                                          @PathVariable("itemId") Long itemId) {

        log.info("Get item {}", itemId);
        return itemClient.getItemById(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItemsUser(@RequestHeader(HEADER_USER) Long userId,
                                                  @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                  @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {

        log.info("List items User {}", userId);
        return itemClient.getItemsUser(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getSearchItem(@RequestParam("text") String text,
                                         @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                         @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {

        log.info("Get item with key substring {}", text);
        return itemClient.searchItem(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader(HEADER_USER) Long userId,
                                             @PathVariable("itemId") Long itemId,
                                             @RequestBody @Valid CommentDto commentDto) {

        log.info("User {} add comment for Item {}", userId, itemId);
        return itemClient.addComment(userId, itemId, commentDto);
    }
}