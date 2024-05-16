package com.battleship.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.battleship.demo.enumeration.Direction;
import com.battleship.demo.model.Board;
import com.battleship.demo.model.Location;
import com.battleship.demo.model.Ship;

@SpringBootTest
class BattleshipApplicationTests {

	@Test
	void contextLoads() {
		
	}

	@Test
	public void testBoard() {
        Board b = new Board();

        Location[] shipLoc = {
            new Location(0, 0),
            new Location(0, 5),
            new Location(1, 0),
            new Location(0, 6),
            new Location(2, 0),
        };

        Direction[] shipDir = {
            Direction.HORIZONTAL,
            Direction.VERTICAL,
            Direction.HORIZONTAL,
            Direction.VERTICAL,
            Direction.HORIZONTAL,
        };

        int[] lengths = {2, 3, 4, 4, 5};

        for(int i = 0; i < 5; i++) {
            b.placeShip(lengths[i], shipLoc[i], shipDir[i]);
        }


        for(int i = 0; i < 5; i++) {
            int row = shipLoc[i].getRow();
            int col = shipLoc[i].getCol();
            for (int j = 0; j < lengths[i]; j++) {
                assert !b.isDefeated();
                b.receiveDamage(new Location(row, col));
                
                if (shipDir[i] == Direction.HORIZONTAL) {
                    col++;
                } else {
                    row++;
                }
            }
        }

        assert b.isDefeated();
    }

	@Test
    public void testLocation() {
        Location l1 = new Location(1, 1);
        Location l2 = new Location(1, 5);
        Location l3 = new Location(5, 1);
        Location l4 = new Location(4, 5);
        Location l5 = new Location(5, 4);
        

        assert l1.colDiff(l2) == 4;
        assert l1.colDiff(l3) == 0;
        assert l1.colDiff(l4) == 4;
        assert l1.colDiff(l5) == 3;

        assert l1.rowDiff(l2) == 0;
        assert l1.rowDiff(l3) == 4;
        assert l1.rowDiff(l4) == 3;
        assert l1.rowDiff(l5) == 4;

        assert l2.colDiff(l1) == -4;
        assert l3.colDiff(l1) == 0;
        assert l4.colDiff(l1) == -4;
        assert l5.colDiff(l1) == -3;

        assert l2.rowDiff(l1) == 0;
        assert l3.rowDiff(l1) == -4;
        assert l4.rowDiff(l1) == -3;
        assert l5.rowDiff(l1) == -4;
    }

	@Test
    public void testShip() {
        Ship s1 = new Ship(5, new Location(1, 1), Direction.HORIZONTAL);
        assert !s1.isDestroyed();
        s1.damage(new Location(1, 1));
        assert !s1.isDestroyed();
        s1.damage(new Location(1, 2));
        assert !s1.isDestroyed();
        s1.damage(new Location(1, 3));
        assert !s1.isDestroyed();
        s1.damage(new Location(1, 4));
        assert !s1.isDestroyed();
        s1.damage(new Location(1, 5));
        assert s1.isDestroyed();
        
        Ship s2 = new Ship(5, new Location(4, 5), Direction.VERTICAL);
        assert !s2.isDestroyed();
        s2.damage(new Location(4, 5));
        assert !s2.isDestroyed();
        s2.damage(new Location(5, 5));
        assert !s2.isDestroyed();
        s2.damage(new Location(6, 5));
        assert !s2.isDestroyed();
        s2.damage(new Location(7, 5));
        assert !s2.isDestroyed();
        s2.damage(new Location(8, 5));
        assert s2.isDestroyed();
    }
}
