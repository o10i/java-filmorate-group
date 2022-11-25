package ru.yandex.practicum.filmorate.storage.feed;

import ru.yandex.practicum.filmorate.model.event.Event;

import java.util.List;

public interface FeedStorage {
    public List<Event> findEventByUserId (Long userId);
    public Event saveEvent(Event event);
}
