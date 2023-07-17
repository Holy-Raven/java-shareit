package ru.practicum.shareit.booking.dto;

import java.util.Optional;

public enum BookingState {

	ALL,

	CURRENT,

	PAST,

	FUTURE,

	WAITING,

	REJECTED;

	public static Optional<BookingState> from(String stringState) {
		for (BookingState state : values()) {
			if (state.name().equalsIgnoreCase(stringState)) {
				return Optional.of(state);
			}
		}
		return Optional.empty();
	}
}
