package com.battleship.demo.enumeration;
public enum Square {
    WATER(' '),
    SHIP('Z'),
    MISSED('O'),
    HIT('X');

    char symbol;

    Square(char symbol) {
        this.symbol = symbol;
    }

    public char getSymbol(boolean hideShips) {
        if(this == SHIP && hideShips)
            return ' ';
        return symbol;
    }
}