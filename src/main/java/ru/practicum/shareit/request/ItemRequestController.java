package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto addRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @RequestBody @Valid ItemRequestDto itemRequestDto) {

        log.info("User {}, add new request", userId);
        return itemRequestService.addRequest(itemRequestDto, userId);
    }
    @GetMapping
    public List<ItemRequestDto> getRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {

        log.info("Get requests by user Id {}", userId);
        return itemRequestService.getRequests(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllRequests(@RequestParam(defaultValue = "0", required = false) Integer from,
                                               @RequestParam(required = false) Integer size) {

        log.info("Get all requests by All users ");
        return itemRequestService.getAllRequests(from, size);
    }


    @GetMapping("/{requestId}")
    public ItemRequestDto getRequestById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @PathVariable("requestId") Long requestId) {

        log.info("Get request {}", requestId);
        return itemRequestService.getRequestById(userId, requestId);
    }
}
