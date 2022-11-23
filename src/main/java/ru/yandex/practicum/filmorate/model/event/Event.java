package ru.yandex.practicum.filmorate.model.event;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.filmorate.model.event.enums.EventType;
import ru.yandex.practicum.filmorate.model.event.enums.Operation;

import javax.validation.constraints.NotNull;

@Data
@Builder
@FieldDefaults(level= AccessLevel.PRIVATE)
public class Event {
    @NotNull
    private Long timestamp;
    @NotNull
    private Long userId;
    @NotNull
    private EventType eventType;
    @NotNull
    private Operation operation;
    private Long eventId;
    @NotNull
    private Long entityId;

    public static Event createEvent(Long userId, EventType eventType, Operation operation, Long entityId) {
        return Event.builder()
                .timestamp(System.currentTimeMillis())
                .userId(userId)
                .eventType(eventType)
                .operation(operation)
                .entityId(entityId)
                .build();
    }
}
