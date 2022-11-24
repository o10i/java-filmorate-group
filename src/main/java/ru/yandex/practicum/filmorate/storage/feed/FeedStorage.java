package ru.yandex.practicum.filmorate.storage.feed;

import ru.yandex.practicum.filmorate.model.event.Event;

import java.util.List;

public interface FeedStorage {
    List<Event> findEventsByUserId (Long userId);
    Event saveEvent(Event event);
}
