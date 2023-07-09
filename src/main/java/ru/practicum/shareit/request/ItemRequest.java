package ru.practicum.shareit.request;

import lombok.*;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "requests", schema = "public")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ItemRequest {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;
}
