import java.util.ArrayList;
import java.util.List;

/**
 * Represents the player in "Mystery Number Challenge Pro".
 * Encapsulates player credentials, lifetime statistics, and achievements.
 */
public class Player {
    // Encapsulated fields
    private String name;
    private int totalGamesPlayed;
    private int gamesWon;
    private int highestScore;
    private int totalAttemptsAcrossWonGames;
    private final List<String> earnedBadges;

    /**
     * Constructor to initialize player details.
     * @param name Name of the player.
     */
    public Player(String name) {
        // Handle empty or default name
        if (name == null || name.trim().isEmpty()) {
            this.name = "MysteryGuesser";
        } else {
            this.name = name.trim();
        }
        this.totalGamesPlayed = 0;
        this.gamesWon = 0;
        this.highestScore = 0;
        this.totalAttemptsAcrossWonGames = 0;
        this.earnedBadges = new ArrayList<>();
    }

    // Encapsulation: Accessors and Mutators

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name.trim();
        }
    }

    public int getTotalGamesPlayed() {
        return totalGamesPlayed;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public int getHighestScore() {
        return highestScore;
    }

    public int getTotalAttemptsAcrossWonGames() {
        return totalAttemptsAcrossWonGames;
    }

    public List<String> getEarnedBadges() {
        // Return a copy to maintain encapsulation integrity
        return new ArrayList<>(earnedBadges);
    }

    /**
     * Calculates the win rate percentage of the player.
     */
    public double getWinRate() {
        if (totalGamesPlayed == 0) {
            return 0.0;
        }
        return ((double) gamesWon / totalGamesPlayed) * 100.0;
    }

    /**
     * Calculates average attempts taken to win a game.
     */
    public double getAverageAttempts() {
        if (gamesWon == 0) {
            return 0.0;
        }
        return (double) totalAttemptsAcrossWonGames / gamesWon;
    }

    /**
     * Records the statistics of a completed game round.
     * @param won True if the player guessed the number, false otherwise.
     * @param score Score achieved in the round.
     * @param attempts Attempts taken in the round.
     */
    public void recordGame(boolean won, int score, int attempts) {
        this.totalGamesPlayed++;
        if (won) {
            this.gamesWon++;
            this.totalAttemptsAcrossWonGames += attempts;
            if (score > this.highestScore) {
                this.highestScore = score;
            }
        }
    }

    /**
     * Attempts to award a badge to the player.
     * @param badge The name of the badge to award.
     * @return true if the badge was newly earned; false if already earned.
     */
    public boolean addBadge(String badge) {
        if (!earnedBadges.contains(badge)) {
            earnedBadges.add(badge);
            return true;
        }
        return false;
    }
}
