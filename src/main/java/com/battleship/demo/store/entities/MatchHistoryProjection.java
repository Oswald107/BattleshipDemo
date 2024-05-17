package com.battleship.demo.store.entities;

public interface MatchHistoryProjection {
    boolean getWinner1();
    String getPlayer1();
    String getPlayer2();
}
