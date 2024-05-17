package com.battleship.demo.store;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.battleship.demo.store.entities.Games;
import com.battleship.demo.store.entities.User;
import com.battleship.demo.store.repository.GamesRepository;
import com.battleship.demo.store.repository.UserRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class StoreController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    GamesRepository gamesRepository;



    @GetMapping("/leaderboard")
    public List<User> getLeaderBoard() {
        return userRepository.findAllByOrderByWinsDesc();
    }

    @GetMapping("/user/{id}")
    public User getUser(@PathVariable("id") int id) {
        return userRepository.findById(id).get();
    }

    @GetMapping("/matches/{id}")
    public List<Games> getMatchHistory(@PathVariable("id") int id) {
        return gamesRepository.findByPlayer1IdOrPlayer2Id(id, id);
    }

    @GetMapping("/match")
    public List<User> getMatch() {
        return userRepository.findAll();
    }
    
    @PostMapping("/user")
    public User addUser(@RequestBody User user) {
        return userRepository.save(user);
    }
}
