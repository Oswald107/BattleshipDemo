package com.battleship.demo.store.entities;

public interface MatchHistoryProjection {
    int getWinner1();
    String getPlayer1();
    String getPlayer2();
}
