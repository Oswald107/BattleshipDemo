package com.battleship.demo.model;

import java.util.UUID;

import com.battleship.demo.Exceptions.PlacementException;
import com.battleship.demo.enumeration.Direction;
import com.battleship.demo.enumeration.GameState;

public class Battleship {
    private String gameId;
    private String player1;
    private String player2;
    private Board player1Board;
    private Board player2Board;
    private String winner;
    private String turn;
    private GameState gameState;

    public Battleship(String player1, String player2) {
        this.gameId = UUID.randomUUID().toString();
        this.player1 = player1;
        this.player2 = player2;
        this.turn = player1;
        player1Board = new Board();
        player2Board = new Board();
        // initializeBoards();
        try {
            player1Board.randomlyPlaceShips();
            player2Board.randomlyPlaceShips();
        } catch (PlacementException e) {

        }
        gameState = GameState.WAITING_FOR_PLAYER;
    }

    // Here for testing purposes
    private void initializeBoards() {
        Location[] shipLoc = {
            new Location(0, 0),
            // new Location(0, 5),
            // new Location(1, 0),
            // new Location(0, 6),
            // new Location(2, 0),
        };

        Direction[] shipDir = {
            Direction.HORIZONTAL,
            // Direction.VERTICAL,
            // Direction.HORIZONTAL,
            // Direction.VERTICAL,
            // Direction.HORIZONTAL,
        };

        int[] lengths = {
            2, 
            // 3, 
            // 4, 
            // 4, 
            // 5
        };

        int numberOfShips = 1;

        for(int i = 0; i < numberOfShips; i++) {
            player1Board.placeShip(lengths[i], shipLoc[i], shipDir[i]);
        }

        for(int i = 0; i < numberOfShips; i++) {
            player2Board.placeShip(lengths[i], shipLoc[i], shipDir[i]);
        }
    }


    public void makeMove(String player, int move) {
        int row = move / 10;
        int col = move % 10;

        if (player.equals(player1)) {
            player2Board.receiveDamage(new Location(row, col));
        }
        else if (player.equals(player2)) {
            player1Board.receiveDamage(new Location(row, col));
        }
        else {
            System.out.println("BAD");
        }
        turn = player.equals(player1) ? player2 : player1;
        checkWinner();
        updateGameState();
    }

    private void checkWinner() {
        if (player1Board.isDefeated())
            winner = player2;
        else if (player2Board.isDefeated())
            winner = player1;
    }

    private void updateGameState() {
        if (winner != null) {
            gameState = winner.equals(player1) ? GameState.PLAYER1_WON : GameState.PLAYER2_WON;
        } else {
            gameState = turn.equals(player1) ? GameState.PLAYER1_TURN : GameState.PLAYER2_TURN;
        }
    }

    public boolean isGameOver() {
        return winner != null;
    }

    /**
     * Getters and Setters
     */
    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String[][] getBoard(String player) {
        if (player ==  player1) {
            return player1Board.getSquares(false);
        }
        return player2Board.getSquares(false);
    }

    public String[][] getEnemyBoard(String player) {
        if (player ==  player1) {
            return player2Board.getSquares(true);
        }
        return player1Board.getSquares(true);
    }

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public String getTurn() {
        return turn;
    }

    public void setTurn(String turn) {
        this.turn = turn;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
}
