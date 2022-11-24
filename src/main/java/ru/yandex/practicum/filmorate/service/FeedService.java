package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.event.Event;
import ru.yandex.practicum.filmorate.storage.feed.FeedStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedService {
    private final FeedStorage feedStorage;
    private final UserService userService;

    public List<Event> findEventsByUserId(Long userId) {
        userService.findUserById(userId);
        return feedStorage.findEventsByUserId(userId);
    }

    public Event saveEvent(Event event) {
        return feedStorage.saveEvent(event);
    }
}
