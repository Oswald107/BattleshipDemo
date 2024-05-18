package com.battleship.demo.store.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import com.battleship.demo.store.entities.GameMatch;
import com.battleship.demo.store.entities.MatchHistoryProjection;


@Repository
public interface GameMatchRepository extends JpaRepository<GameMatch, Integer> {

    @Query("SELECT gm.winner1 as winner1, u1.username AS player1, u2.username AS player2 " +
           "FROM GameMatch gm " +
           "JOIN User u1 ON gm.player1Id = u1.id " +
           "JOIN User u2 ON gm.player2Id = u2.id " +
           "WHERE gm.player1Id = :playerId OR gm.player2Id = :playerId")
    List<MatchHistoryProjection> findMatchHistory(int playerId);
}