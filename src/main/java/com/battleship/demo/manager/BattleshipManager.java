package com.battleship.demo.manager;

import com.battleship.demo.enumeration.GameState;
import com.battleship.demo.model.Battleship;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BattleshipManager {

    private final Map<String, Battleship> games;
    protected final Map<String, String> waitingPlayers;

    public BattleshipManager() {
        games = new ConcurrentHashMap<>();
        waitingPlayers = new ConcurrentHashMap<>();
    }

    public synchronized Battleship joinGame(String player) {
        if (games.values().stream().anyMatch(game -> player.equals(game.getPlayer1()) || (game.getPlayer2() != null && player.equals(game.getPlayer2())))) {
            return games.values().stream().filter(game -> player.equals(game.getPlayer1()) || player.equals(game.getPlayer2())).findFirst().get();
        }

        for (Battleship game : games.values()) {
            if (game.getPlayer1() != null && game.getPlayer2() == null) {
                game.setPlayer2(player);
                game.setGameState(GameState.PLAYER1_TURN);
                return game;
            }
        }

        Battleship game = new Battleship(player, null);
        games.put(game.getGameId(), game);
        waitingPlayers.put(player, game.getGameId());
        return game;
    }

    public synchronized Battleship leaveGame(String player) {
        String gameId = getGameByPlayer(player) != null ? getGameByPlayer(player).getGameId() : null;
        if (gameId != null) {
            waitingPlayers.remove(player);
            Battleship game = games.get(gameId);
            if (player.equals(game.getPlayer1())) {
                if (game.getPlayer2() != null) {
                    game.setPlayer1(game.getPlayer2());
                    game.setPlayer2(null);
                    game.setGameState(GameState.WAITING_FOR_PLAYER);
                    waitingPlayers.put(game.getPlayer1(), game.getGameId());
                } else {
                    games.remove(gameId);
                    return null;
                }
            } else if (player.equals(game.getPlayer2())) {
                game.setPlayer2(null);
                game.setGameState(GameState.WAITING_FOR_PLAYER);
                waitingPlayers.put(game.getPlayer1(), game.getGameId());
            }
            return game;
        }
        return null;
    }

    public Battleship getGame(String gameId) {
        return games.get(gameId);
    }

    public Battleship getGameByPlayer(String player) {
        return games.values().stream().filter(game -> player.equals(game.getPlayer1()) || (game.getPlayer2() != null &&
                player.equals(game.getPlayer2()))).findFirst().orElse(null);
    }

    public void removeGame(String gameId) {
        games.remove(gameId);
    }
}