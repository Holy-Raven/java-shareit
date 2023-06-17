package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

interface ItemRepository extends JpaRepository<Item, Long> {


    List<Item> findByOwnerId(long userId);

    @Query("select i from Item i where upper(i.name) like upper(concat('%', ?1, '%')) or upper(i.description) like upper(concat('%', ?1, '%')) and i.available = true ")
    List<Item> search (String text);


}