package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

/**
 * TODO Sprint add-controllers.
 */

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Item {

    @EqualsAndHashCode.Include
    @Positive(message = "Id must be positive")
    private long id;

    @NotNull(message = "Name cannot be empty or contain spaces.")
    @NotBlank(message = "Name cannot be empty or contain spaces.")
    private String name;

    @NotNull(message = "Description cannot be empty or contain spaces.")
    @NotBlank(message = "Name cannot be empty or contain spaces.")
    @Size(max = 200, message = "The maximum length of the description should not exceed 200 characters")
    private String description;

    @NotNull
    private Boolean available;

    private User owner;

    private long count;

    private final Set<Item> itemSet = new HashSet<>();

    public long addCount() {
        return count++;
    }

    public void addItemInSet(Item item) {
        itemSet.add(item);
    }

}
