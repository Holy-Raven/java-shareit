package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ItemRequestDto {

    private long id;

    @NotNull(message = "Description cannot be empty")
    @NotBlank(message = "Description cannot be blank")
    @NotEmpty
    private String description;

    private LocalDateTime created;

    private List<ItemDto> items;
}