package com.battleship.demo.store;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.battleship.demo.store.entities.GameMatch;
import com.battleship.demo.store.entities.MatchHistoryProjection;
import com.battleship.demo.store.entities.User;
import com.battleship.demo.store.repository.GameMatchRepository;
import com.battleship.demo.store.repository.UserRepository;

@Service
public class StoreService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    GameMatchRepository gameMatchRepository;

    
    public void addMatch(GameMatch game) {
        gameMatchRepository.save(game);
    }
    // public List<Games> getMatchHistory(int id) {
    //     return gamesRepository.findByPlayer1IdOrPlayer2Id(id, id);
    // }

    public String[] getMatchHistory(int id) {
        List<MatchHistoryProjection> matches = gameMatchRepository.findMatchHistory(id);
        String[] output = new String[matches.size()];
        for (int i = 0; i < matches.size(); i++) {
            MatchHistoryProjection match = matches.get(i);
            output[i] = match.getPlayer1() + "\t" + match.getPlayer2() + "\t" + (match.getWinner1() ? match.getPlayer1() : match.getPlayer2());
        }
        return output;
    }



    public void incWins(String name) {
        User user = userRepository.findByUsername(name);
        user.incWins();
        userRepository.save(user);
    }
    public void incLosses(String name) {
        User user = userRepository.findByUsername(name);
        user.incLosses();
        userRepository.save(user);
    }

    // get user if exists, otherwise
    public User getOrCreateUser(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            user = userRepository.save(new User(username));
        }
        return user;
    }
    public String[] getLeaderBoard() {
        List<User> users = userRepository.findAllByOrderByWinsDesc();
        String[] output = new String[users.size()];
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            output[i] = user.getUsername() + "\t" + user.getWins() + "\t" + user.getLosses();
        }
        return output;
    }


    public User getUser(String name) {
        return userRepository.findByUsername(name);
    }
    public void addUser(User user) {
        userRepository.save(user);
    }

    // public User incrementUserWins(int id) {
    //     Optional<User> optionalUser = userRepository.findById(id);
    //     if (optionalUser.isPresent()) {
    //         User user = optionalUser.get();
    //         user.setWins(user.getWins() + 1);
    //         return userRepository.save(user);
    //     } else {
    //         throw new RuntimeException("User not found with id: " + id);
    //     }
    // }
}
