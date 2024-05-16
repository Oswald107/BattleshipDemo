package com.battleship.demo.model;
import com.battleship.demo.enumeration.Square;
import com.battleship.demo.enumeration.Direction;

public class Board {
    private final int size = 10;
    private final int numberOfShips = 1;
    private Square[][] squares;
    private Ship[] ships;

    public Board(){
        squares = new Square[size][size];
        ships = new Ship[numberOfShips];
    }

    public void placeShip(int length, Location loc, Direction d) {
        if (loc.getRow() < 0 && loc.getRow() >= size && loc.getCol() < 0 && loc.getCol() >= size) {
            System.out.println("Ship cannot be placed here. Location is outside board bounds.");
            return;
        }

        if ((d == Direction.HORIZONTAL && loc.getCol() + length > size) ||
            (d == Direction.VERTICAL && loc.getRow() + length > size)) {
            System.out.println("Ship cannot be placed here due to board bounds.");
            return;
        }
        
        if (collision(length, loc, d)) {
            System.out.println("Ship cannot be placed here due to ship collision.");
            return;
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


    public void enemyDisplay() {
        for(Square[] row : squares) {
            for (Square val : row) {
                if (val == Square.MISSED)
                    System.out.print("o");
                else if (val == Square.HIT)
                    System.out.print("x");
                else
                    System.out.print(".");
            }
            System.out.println();
        }
    }

    public void selfDisplay() {
        for(Square[] row : squares) {
            for (Square val : row) {
                if (val == Square.MISSED)
                    System.out.print("o");
                else if (val == Square.HIT)
                    System.out.print("x");
                else if (val == Square.SHIP)
                    System.out.print("z");
                else
                    System.out.print(".");
            }
            System.out.println();
        }
    }

    public String[][] getSquaresAsEnemy() {
        String[][] output = new String[10][10];
        for(int row = 0; row < 10; row++) {
            for(int col = 0; col < 10; col++) {
                Square val = squares[row][col];
                if (val == Square.MISSED)
                    output[row][col] = "o";
                else if (val == Square.HIT)
                    output[row][col] = "x";
                else
                    output[row][col] = " ";
            }
        }
        return output;
    }

    public String[][] getSquaresAsSelf() {
        String[][] output = new String[10][10];
        for(int row = 0; row < 10; row++) {
            for(int col = 0; col < 10; col++) {
                Square val = squares[row][col];
                if (val == Square.MISSED)
                    output[row][col] = "o";
                else if (val == Square.HIT)
                    output[row][col] = "x";
                else if (val == Square.SHIP)
                    output[row][col] = "z";
                else
                    output[row][col] = " ";
            }
        }
        return output;
    }

    public char[][] getSquares(boolean hideShips) {
        char[][] output = new char[10][10];
        for(int row = 0; row < 10; row++) {
            for(int col = 0; col < 10; col++) {
                output[row][col] = squares[row][col].getSymbol(hideShips);
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