package Stats;

public class Match {
    private String player1;
    private String player2;
    private boolean player1Wins;

    public Match(String player1, String player2, boolean player1Wins) {
        this.player1 = player1;
        this.player2 = player2;
        this.player1Wins = player1Wins;
    }

    public String getPlayer1() {
        return player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public boolean isPlayer1Wins() {
        return player1Wins;
    }
}