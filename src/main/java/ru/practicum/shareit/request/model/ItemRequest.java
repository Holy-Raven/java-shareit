package ru.practicum.shareit.request.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * TODO Sprint add-item-requests.
 */

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ItemRequest {

    @EqualsAndHashCode.Include
    Long id;

    String description;

    long requestor;

    LocalDate created;

}
