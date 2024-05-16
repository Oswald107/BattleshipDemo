package com.battleship.demo.model;
public class Location {
    private int row;
    private int col;

    public Location(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int rowDiff(Location l2) {
        return l2.getRow() - row;
    }

    public int colDiff(Location l2) {
        return l2.getCol() - col;
    }
}
