package ru.practicum.shareit.user.model;

import lombok.*;

import javax.persistence.*;

/**
 * TODO Sprint add-controllers.
 */
//@Data
//@Builder
//@EqualsAndHashCode(onlyExplicitlyIncluded = true)
//public class User {
//
//    @EqualsAndHashCode.Include
//    private long id;
//
//    private String name;
//
//    private String email;
//}


@Entity
@Table(name = "users", schema = "public")
@Getter @Setter @ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private long id;

    private String name;

    private String email;
    public User() {
    }
}
