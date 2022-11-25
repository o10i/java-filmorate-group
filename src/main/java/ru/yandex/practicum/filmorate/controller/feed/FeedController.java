package ru.yandex.practicum.filmorate.controller.feed;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.event.Event;
import ru.yandex.practicum.filmorate.service.FeedService;

import java.util.List;

@RestController
@RequestMapping("/users/{id}/feed")
@RequiredArgsConstructor
public class FeedController {
    private final FeedService feedService;

    @GetMapping
    public List<Event> findEventByUserId(@PathVariable("id") Long eventId){
        return feedService.findEventByUserId(eventId);
    }
}
