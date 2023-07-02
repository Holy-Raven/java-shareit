package ru.practicum.shareit.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByOwnerId(long userId);

    List<Item> findByRequestId(long requestId);

    Page<Item> findByOwnerId(long userId, PageRequest pageRequest);

    @Query("select i from Item i where upper(i.name) like upper(concat('%', ?1, '%')) or upper(i.description) like upper(concat('%', ?1, '%')) and i.available = true ")
    Page<Item> search(String text, PageRequest pageRequest);
}