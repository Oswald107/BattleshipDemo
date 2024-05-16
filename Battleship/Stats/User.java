package Stats;

public class User {
    private String username;
    private int wins;
    private int losses;

    public User(String username) {
        this.username = username;
        this.wins = 0;
        this.losses = 0;
    }

    public String getUsername() {
        return username;
    }

    public int getWins() {
        return wins;
    }

    public void win() {
        wins++;
    }

    public int getLosses() {
        return losses;
    }

    public void lose() {
        losses++;
    }
}