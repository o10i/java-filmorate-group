package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.DataService;

import java.util.List;

@RestController
public class DataController {

    private final DataService dataService;

    @Autowired
    public DataController(DataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping("/genres")
    public List<Genre> findAllGenres(){
        return dataService.findAllGenre();
    }

    @GetMapping("/genres/{id}")
    public Genre findGenreById(@PathVariable("id") Long genreId){
        return dataService.findGenreById(genreId);
    }

    @GetMapping("/mpa")
    public List<MPA> findAllMPA(){
        return dataService.findAllMPA();
    }

    @GetMapping("/mpa/{id}")
    public MPA findMPAById(@PathVariable("id") Long MPAId){
        return dataService.findMPAById(MPAId);
    }
}
