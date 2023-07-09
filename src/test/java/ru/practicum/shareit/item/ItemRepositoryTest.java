package ru.practicum.shareit.item;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ItemRepository itemRepository;

    Item firstitem;

    Item secondItem;

    User user;

    @BeforeEach
    void beforeEach() {

        user = userRepository.save(User.builder()
                .id(1L)
                .name("Anna")
                .email("anna@yandex.ru")
                .build());

        firstitem = itemRepository.save(Item.builder()
                .name("screwdriver")
                .description("works well, does not ask to eat")
                .available(true)
                .owner(user)
                .build());

        secondItem = itemRepository.save(Item.builder()
                .name("guitar")
                .description("a very good tool")
                .available(true)
                .owner(user)
                .build());
    }

    @Test
    void search() {

        List<Item> items = itemRepository.search("very", PageRequest.of(0, 1));

        assertEquals(1, items.size());
        assertEquals("guitar", items.get(0).getName());
    }

    @AfterEach
    void afterEach() {
        userRepository.deleteAll();
        itemRepository.deleteAll();
    }
}

