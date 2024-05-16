package com.battleship.demo.model;
import com.battleship.demo.enumeration.Direction;

public class Ship {
    private int length;
    private boolean[] hit;
    private Location location;
    private Direction direction;

    public Ship(int length, Location location, Direction direction) {
        this.length = length;
        this.hit = new boolean[length];
        this.location = location;
        this.direction = direction;
    }

    public boolean damage(Location missleLocation) {
        int rd = location.rowDiff(missleLocation);
        int cd = location.colDiff(missleLocation);
        if (direction == Direction.HORIZONTAL && rd==0 && cd>=0 && cd<length) {
            hit[cd] = true;
            return true;
        }
        if (direction == Direction.VERTICAL && cd==0 && rd>=0 && rd<length) {
            hit[rd] = true;
            return true;
        }

        return false;
    }

    public boolean isDestroyed() {
        for(boolean b : hit) {
            if (!b)
                return false;
        }
        return true;
    }
}
