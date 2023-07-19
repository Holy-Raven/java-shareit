package ru.practicum.shareit.booking.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;

import lombok.*;

@Data
@Builder
public class BookingDto {

	private Long itemId;

	@NotNull(message = "start cannot be empty.")
	@FutureOrPresent(message = "start may be in the present or future")
	private LocalDateTime start;

	@NotNull(message = "end cannot be empty.")
	@Future(message = "end may be in the future")
	private LocalDateTime end;

}
