package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;
import java.util.List;

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


//    @GetMapping
//    public List<Item> get(@RequestHeader("X-Later-User-Id") long userId) {
//        return itemService.getItems(userId);
//    }

    @PostMapping
    public ItemDto add(@RequestHeader("X-Sharer-User-Id") long owner,
                    @RequestBody @Valid ItemDto itemDto) {

        Item item = mapper.returnItem(itemDto);

        itemService.addNewItem(owner, item);

        return mapper.returnItemDto(item);
    }


//    @PostMapping
//    public Item add(@RequestHeader("X-Sharer-User-Id") long owner,
//                       @RequestBody @Valid Item item) {
//        return itemService.addNewItem(owner, item);
//    }


//    @DeleteMapping("/{itemId}")
//    public void deleteItem(@RequestHeader("X-Later-User-Id") long userId,
//                           @PathVariable long itemId) {
//        itemService.deleteItem(userId, itemId);
//    }
}