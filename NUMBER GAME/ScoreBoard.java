import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Manages the top 5 scores during the application execution.
 * Formats and prints the leaderboard in a terminal-friendly grid.
 */
public class ScoreBoard {
    private final List<LeaderboardEntry> topScores;
    private static final int MAX_ENTRIES = 5;

    public ScoreBoard() {
        this.topScores = new ArrayList<>();
    }

    /**
     * Inner class representing an entry on the leaderboard.
     */
    public static class LeaderboardEntry implements Comparable<LeaderboardEntry> {
        private final String playerName;
        private final int score;
        private final String dateString;

        public LeaderboardEntry(String playerName, int score) {
            this.playerName = playerName;
            this.score = score;
            // Get current date/time for timestamping scores
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            this.dateString = sdf.format(new Date());
        }

        public String getPlayerName() {
            return playerName;
        }

        public int getScore() {
            return score;
        }

        public String getDateString() {
            return dateString;
        }

        @Override
        public int compareTo(LeaderboardEntry other) {
            // Sort in descending order (highest score first)
            return Integer.compare(other.score, this.score);
        }
    }

    /**
     * Adds a player's score to the leaderboard.
     * Keeps only the top 5 scores.
     */
    public void addScore(String playerName, int score) {
        LeaderboardEntry newEntry = new LeaderboardEntry(playerName, score);
        topScores.add(newEntry);
        Collections.sort(topScores);
        
        // Truncate to top 5
        while (topScores.size() > MAX_ENTRIES) {
            topScores.remove(topScores.size() - 1);
        }
    }

    /**
     * Displays the leaderboard in a highly polished ASCII visual frame.
     */
    public void displayLeaderboard() {
        String cyan = "\u001B[36m";
        String yellow = "\u001B[33m";
        String reset = "\u001B[0m";
        String bold = "\u001B[1m";
        String gold = "\u001B[33m\u001B[1m"; // Bold Yellow for rank 1
        String silver = "\u001B[37m\u001B[1m"; // Bold White for rank 2
        String bronze = "\u001B[31m\u001B[1m"; // Bold Red/Orange for rank 3 (we can use red for dark orange)
        
        System.out.println("\n" + cyan + "╔════════════════════════════════════════════════════════════╗" + reset);
        System.out.println(cyan + "║" + yellow + bold + "              🏆 MYSTERY NUMBER HALL OF FAME 🏆             " + cyan + "║" + reset);
        System.out.println(cyan + "╠════════════════════════════════════════════════════════════╣" + reset);
        System.out.printf(cyan + "║ %-4s | %-18s | %-8s | %-16s ║\n" + reset, "Rank", "Player Name", "Score", "Date/Time Earned");
        System.out.println(cyan + "╠════════════════════════════════════════════════════════════╣" + reset);
        
        if (topScores.isEmpty()) {
            System.out.printf(cyan + "║ %-56s ║\n" + reset, " No records yet! Play a round to establish a high score.");
        } else {
            for (int i = 0; i < topScores.size(); i++) {
                LeaderboardEntry entry = topScores.get(i);
                String rankStr = String.valueOf(i + 1);
                
                // Colorize based on rank
                String rowColor;
                if (i == 0) {
                    rowColor = gold;
                    rankStr = "👑 1";
                } else if (i == 1) {
                    rowColor = silver;
                    rankStr = "🥈 2";
                } else if (i == 2) {
                    rowColor = bronze;
                    rankStr = "🥉 3";
                } else {
                    rowColor = reset;
                    rankStr = "  " + rankStr;
                }
                
                // Clean output layout
                System.out.printf(cyan + "║ " + rowColor + "%-4s" + cyan + " | " + rowColor + "%-18s" + cyan + " | " + rowColor + "%-8d" + cyan + " | " + rowColor + "%-16s" + cyan + " ║\n" + reset,
                        rankStr, entry.getPlayerName(), entry.getScore(), entry.getDateString());
            }
        }
        
        System.out.println(cyan + "╚════════════════════════════════════════════════════════════╝" + reset);
    }
}
