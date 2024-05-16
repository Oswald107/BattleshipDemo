package Stats;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Leaderboard {
    private List<User> users;

    public Leaderboard() {
        users = new ArrayList<>();
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void updateUserWins(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                user.win();
                return;
            }
        }
    }

    public void updateUserLosses(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                user.lose();
                return;
            }
        }
    }

    public void displayLeaderboard() {
        // Sort users based on wins (descending order)
        Collections.sort(users, Comparator.comparingInt(User::getWins).reversed());

        // Display the leaderboard
        System.out.println("Leaderboard:");
        System.out.println("Username\tWins\tLosses");
        for (User user : users) {
            System.out.println(user.getUsername() + "\t\t" + user.getWins() + "\t\t" + user.getLosses());
        }
    }
}