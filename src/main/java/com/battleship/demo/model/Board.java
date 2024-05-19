package com.battleship.demo.model;
import com.battleship.demo.enumeration.Square;

import java.util.Random;

import com.battleship.demo.Exceptions.PlacementException;
import com.battleship.demo.enumeration.Direction;

public class Board {
    private final int size = 10;
    private final int numberOfShips = 5;
    private Square[][] squares;
    private Ship[] ships;

    public Board(){
        squares = new Square[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                squares[i][j] = Square.WATER;
            }
        }
        ships = new Ship[numberOfShips];
    }

    public void randomlyPlaceShips() throws PlacementException {
        int[] shipLengths = {5, 4, 4, 3, 2};
        Random rand = new Random();
        int maxAttempts = 100;

        for(int len : shipLengths) {
            boolean placed = false;
            int attempt = 0;
            while (!placed && attempt < maxAttempts) {
                Direction dir = rand.nextBoolean() ? Direction.HORIZONTAL : Direction.VERTICAL;
                int x, y;
                if (dir == Direction.HORIZONTAL) {
                    x = rand.nextInt(size-len+1);
                    y = rand.nextInt(10);
                } else {
                    x = rand.nextInt(10);
                    y = rand.nextInt(size-len+1);
                }

                Location loc = new Location(x, y);


                if (placeShip(len, loc, dir)) {
                    placed = true;
                }
                attempt++;
            }

            if (!placed) {
                throw new PlacementException("Failed to place ships");
            }
        }
    }

    public boolean placeShip(int length, Location loc, Direction d) {
        // Check that initial Location is in bounds
        if (loc.getRow() < 0 && loc.getRow() >= size && loc.getCol() < 0 && loc.getCol() >= size) {
            return false;
        }

        // Check that ship doesn't protrud out of board bounds
        if ((d == Direction.HORIZONTAL && loc.getCol() + length > size) ||
            (d == Direction.VERTICAL && loc.getRow() + length > size)) {
            return false;
        }
        
        // Check that ship doesn't collide with other ships
        if (collision(length, loc, d)) {
            return false;
        }
        
        // Add the ship to the board squares
        int row = loc.getRow();
        int col = loc.getCol();
        for (int i = 0; i < length; i++) {
            squares[row][col] = Square.SHIP;
            if (d == Direction.HORIZONTAL) {
                col++;
            } else {
                row++;
            }
        }
        
        // Add the ship to the ships array
        for (int i = 0; i < ships.length; i++) {
            if (ships[i] == null) {
                ships[i] = new Ship(length, loc, d);;
                break;
            }
        }

        return true;
    }

    private boolean collision(int length, Location loc, Direction d) {
        int row = loc.getRow();
        int col = loc.getCol();
        for (int i = 0; i < length; i++) {
            if (squares[row][col] == Square.SHIP) {
                return true;
            }
            if (d == Direction.HORIZONTAL) {
                col++;
            } else {
                row++;
            }
        }
        return false;
    }

    public String[][] getSquares(boolean hideShips) {
        String[][] output = new String[10][10];
        for(int row = 0; row < 10; row++) {
            for(int col = 0; col < 10; col++) {
                output[row][col] = ""+squares[row][col].getSymbol(hideShips);
            }
        }
        return output;
    }


    public void receiveDamage(Location missleLocation) {
        for(Ship ship : ships) {
            if (ship.damage(missleLocation)) {
                squares[missleLocation.getRow()][missleLocation.getCol()] = Square.HIT;
                return;
            } else {
                squares[missleLocation.getRow()][missleLocation.getCol()] = Square.MISSED;
            }
        }
    }

    public boolean isDefeated() {
        for (Ship ship : ships) {
            if(!ship.isDestroyed())
                return false;
        }
        return true;
    }
}