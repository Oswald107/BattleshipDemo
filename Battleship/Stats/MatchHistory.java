package Stats;

import java.util.ArrayList;
import java.util.List;

public class MatchHistory {
    private List<Match> matches;

    public MatchHistory() {
        matches = new ArrayList<>();
    }

    public void append(Match match) {
        matches.add(match);
    }

    public int size() {
        return matches.size();
    }

    public Match getMatch(int index) {
        if (index >= 0 && index < matches.size()) {
            return matches.get(index);
        } else {
            throw new IndexOutOfBoundsException("Index " + index + " is out of bounds for match history");
        }
    }
}