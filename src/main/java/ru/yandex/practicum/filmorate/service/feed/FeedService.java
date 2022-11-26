package ru.yandex.practicum.filmorate.service.feed;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.event.Event;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.feed.FeedStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FeedService {
    FeedStorage feedStorage;
    UserService userService;

    public List<Event> findEventsByUserId(Long userId) {
        userService.findUserById(userId);
        return feedStorage.findEventsByUserId(userId);
    }

    public Event saveEvent(Event event) {
        return feedStorage.saveEvent(event);
    }
}
