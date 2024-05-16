public class Game {
    private Board b1;
    private Board b2;

    public Game() {
        b1 = new Board();
        b2 = new Board();
    }

    public void play() {
        while (true) {
            // p1 turn
            
            // p2 defeated?

            // p2 turn
            
            // p1 defeated?
        }
    }


    public static void main(String[] args) {
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

        Game g = new Game();

        for(int i = 0; i < 5; i++) {
            g.b1.placeShip(lengths[i], shipLoc[i], shipDir[i]);
        }

        for(int i = 0; i < 5; i++) {
            g.b2.placeShip(lengths[i], shipLoc[i], shipDir[i]);
        }
  
        System.out.println(g.b1.isDefeated());
        for (int i = 0; i < 4; i++) {
            for(int j = 0; j < 7; j++) {
                g.b1.receiveDamage(new Location(i, j));
            }

            
            System.out.println("Self");
            g.b1.selfDisplay();
            System.out.println("Enemy");
            g.b1.enemyDisplay();
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
            
        }
        System.out.println(g.b1.isDefeated());
    }
}
