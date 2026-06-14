import java.util.Random;
import java.util.Scanner;

/**
 * Handles a single round of "Mystery Number Challenge Pro".
 * Manages the generation of the secret number, tracking attempts, evaluating guesses,
 * displaying progress, calculation of score, and badge criteria.
 */
public class Game {
    
    // Difficulty Settings Enum
    public enum Difficulty {
        EASY("Easy", 1, 50, 15),
        MEDIUM("Medium", 1, 100, 10),
        HARD("Hard", 1, 500, 8);

        private final String label;
        private final int min;
        private final int max;
        private final int maxAttempts;

        Difficulty(String label, int min, int max, int maxAttempts) {
            this.label = label;
            this.min = min;
            this.max = max;
            this.maxAttempts = maxAttempts;
        }

        public String getLabel() { return label; }
        public int getMin() { return min; }
        public int getMax() { return max; }
        public int getMaxAttempts() { return maxAttempts; }
    }

    // Core Game Fields
    private final Player player;
    private final Difficulty difficulty;
    private final int secretNumber;
    private final int maxAttempts;
    private int attemptsTaken;
    private int score;
    private boolean won;
    
    // ANSI Color Constants for Terminal UI
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String MAGENTA = "\u001B[35m";
    private static final String CYAN = "\u001B[36m";
    private static final String BOLD = "\u001B[1m";

    /**
     * Constructor for Game.
     * @param player The active Player object.
     * @param difficulty The chosen game difficulty.
     */
    public Game(Player player, Difficulty difficulty) {
        this.player = player;
        this.difficulty = difficulty;
        this.maxAttempts = difficulty.getMaxAttempts();
        this.attemptsTaken = 0;
        this.score = 100; // Base score
        this.won = false;

        // Generate random number in range [min, max]
        Random rand = new Random();
        this.secretNumber = rand.nextInt((difficulty.getMax() - difficulty.getMin()) + 1) + difficulty.getMin();
    }

    /**
     * Executes the gameplay loop.
     * @param scanner Shared Scanner from Main.
     * @return The final score of this round (0 if lost).
     */
    public int play(Scanner scanner) {
        System.out.println("\n" + CYAN + "╔════════════════════════════════════════════════════════════╗" + RESET);
        System.out.println(CYAN + "║" + BOLD + MAGENTA + "                  INITIATING CHALLENGE                      " + CYAN + "║" + RESET);
        System.out.println(CYAN + "╠════════════════════════════════════════════════════════════╣" + RESET);
        System.out.printf(CYAN + "║  " + RESET + "%-56s" + CYAN + "║\n" + RESET, "Target: Guess the secret number!");
        System.out.printf(CYAN + "║  " + RESET + "Difficulty: " + BOLD + YELLOW + "%-8s" + RESET + " Range: " + BOLD + GREEN + "%-3d to %-3d" + RESET + "                     " + CYAN + "║\n" + RESET, 
                          difficulty.getLabel(), difficulty.getMin(), difficulty.getMax());
        System.out.printf(CYAN + "║  " + RESET + "Allowed Attempts: " + BOLD + CYAN + "%-2d" + RESET + "                                       " + CYAN + "║\n" + RESET, maxAttempts);
        System.out.println(CYAN + "╚════════════════════════════════════════════════════════════╝" + RESET);

        while (attemptsTaken < maxAttempts) {
            displayProgressBar();
            System.out.print(BOLD + "👉 Enter your guess: " + RESET);
            
            String input = scanner.nextLine();
            int guess;
            
            // Exception handling for invalid integer input
            try {
                guess = Integer.parseInt(input.trim());
            } catch (NumberFormatException e) {
                System.out.println(RED + "⚠ INVALID INPUT: Please enter a whole number." + RESET);
                continue; // Do not penalize or count as attempt
            }

            // Exception handling for out-of-bounds inputs
            if (guess < difficulty.getMin() || guess > difficulty.getMax()) {
                System.out.println(RED + "⚠ OUT OF BOUNDS: Your guess must be between " 
                        + difficulty.getMin() + " and " + difficulty.getMax() + "." + RESET);
                continue; // Do not penalize or count as attempt
            }

            // Increment attempt since input is valid
            attemptsTaken++;

            if (guess == secretNumber) {
                won = true;
                break;
            } else {
                giveIntelligentHint(guess);
            }
        }

        calculateFinalScore();
        displaySummary();
        
        // Record details in Player statistics
        player.recordGame(won, score, attemptsTaken);
        
        // Return score to main to evaluate leaderboard entries
        return won ? score : 0;
    }

    /**
     * Prints a dynamic visual indicator of remaining attempts using ANSI colors.
     */
    private void displayProgressBar() {
        int remaining = maxAttempts - attemptsTaken;
        StringBuilder bar = new StringBuilder();
        
        // Visual indicator of attempts used (represented by filled blocks)
        for (int i = 0; i < maxAttempts; i++) {
            if (i < attemptsTaken) {
                bar.append("█");
            } else {
                bar.append("░");
            }
        }
        
        // Dynamic coloring of status line
        String color;
        if (remaining > maxAttempts / 2) {
            color = GREEN;
        } else if (remaining > 2) {
            color = YELLOW;
        } else {
            color = RED;
        }

        System.out.println("\n" + BOLD + "Status: " + color + "[" + bar + "] " 
                + remaining + "/" + maxAttempts + " attempts left" + RESET);
    }

    /**
     * Assesses the difference and prints high/low/proximity alerts.
     */
    private void giveIntelligentHint(int guess) {
        int diff = Math.abs(guess - secretNumber);
        String direction = (guess > secretNumber) ? "Too High" : "Too Low";
        String trendSymbol = (guess > secretNumber) ? "📈" : "📉";
        
        System.out.print("💡 " + BOLD + "HINT: " + RESET);
        if (diff <= 2) {
            System.out.println(RED + direction + " (Extremely Close! 🔴 Difference <= 2)" + RESET);
        } else if (diff <= 5) {
            System.out.println(YELLOW + direction + " (Very Close! 🟡 Difference <= 5)" + RESET);
        } else {
            System.out.println(BLUE + direction + " " + trendSymbol + RESET);
        }
    }

    /**
     * Evaluates final score based on attempts and rules.
     */
    private void calculateFinalScore() {
        if (!won) {
            score = 0;
            return;
        }
        // Start with 100
        // Deduct 10 points for every WRONG guess (i.e. attemptsTaken - 1)
        int wrongGuesses = attemptsTaken - 1;
        score -= (wrongGuesses * 10);
        
        // Clamp score at a minimum of 10 points if they successfully guessed
        // to reward completion.
        if (score < 10) {
            score = 10;
        }

        // Add 20 points bonus if guessed within first 3 attempts
        if (attemptsTaken <= 3) {
            score += 20;
        }
    }

    /**
     * Checks achievement conditions and adds them to Player profile.
     * Prints an alert if a new badge is unlocked.
     */
    private void checkAndAwardBadges(StringBuilder badgesLog) {
        if (!won) return;

        // Badge 1: Lucky Guess
        if (attemptsTaken == 1) {
            if (player.addBadge("Lucky Guess")) {
                badgesLog.append("   ⭐ [Lucky Guess] - Guessed the number on the 1st attempt!\n");
            }
        }

        // Badge 2: Sharp Shooter
        if (attemptsTaken <= 3) {
            if (player.addBadge("Sharp Shooter")) {
                badgesLog.append("   🎯 [Sharp Shooter] - Found the number within 3 attempts!\n");
            }
        }

        // Badge 3: Persistent Player
        if (attemptsTaken == maxAttempts) {
            if (player.addBadge("Persistent Player")) {
                badgesLog.append("   🛡 [Persistent Player] - Won on the absolute final attempt!\n");
            }
        }
    }

    /**
     * Renders a highly formatted card detailing this round's statistics and outcomes.
     */
    private void displaySummary() {
        System.out.println("\n" + CYAN + "╔════════════════════════════════════════════════════════════╗" + RESET);
        if (won) {
            System.out.println(CYAN + "║" + BOLD + GREEN + "                🎉 STAGE CLEAR: YOU WON! 🎉                 " + CYAN + "║" + RESET);
        } else {
            System.out.println(CYAN + "║" + BOLD + RED + "                💀 GAME OVER: DEFEAT 💀                    " + CYAN + "║" + RESET);
        }
        System.out.println(CYAN + "╠════════════════════════════════════════════════════════════╣" + RESET);
        
        System.out.printf(CYAN + "║  " + RESET + "Secret Number: " + BOLD + YELLOW + "%-3d" + RESET + "                                         " + CYAN + "║\n" + RESET, secretNumber);
        System.out.printf(CYAN + "║  " + RESET + "Attempts Used: " + BOLD + CYAN + "%-2d / %-2d" + RESET + "                                      " + CYAN + "║\n" + RESET, attemptsTaken, maxAttempts);
        
        // Detailed points calculation breakdown
        if (won) {
            int wrongGuesses = attemptsTaken - 1;
            int baseScore = 100;
            int penalty = wrongGuesses * 10;
            int bonus = (attemptsTaken <= 3) ? 20 : 0;
            
            System.out.printf(CYAN + "║  " + RESET + "Base Points: " + GREEN + "%-3d" + RESET + "                                          " + CYAN + "║\n" + RESET, baseScore);
            System.out.printf(CYAN + "║  " + RESET + "Penalties (Wrong Guesses x10): " + RED + "-%-3d" + RESET + "                         " + CYAN + "║\n" + RESET, penalty);
            System.out.printf(CYAN + "║  " + RESET + "Bonus Points (Guessed within 3): " + GREEN + "+%-2d" + RESET + "                       " + CYAN + "║\n" + RESET, bonus);
            System.out.printf(CYAN + "║  " + RESET + "Final Round Score: " + BOLD + GREEN + "%-3d points" + RESET + "                             " + CYAN + "║\n" + RESET, score);
        } else {
            System.out.printf(CYAN + "║  " + RESET + "Round Score: " + BOLD + RED + "%-3d points (Deducted on fail)" + RESET + "               " + CYAN + "║\n" + RESET, 0);
        }
        
        // Evaluate Badges
        StringBuilder badgesLog = new StringBuilder();
        checkAndAwardBadges(badgesLog);
        
        if (badgesLog.length() > 0) {
            System.out.println(CYAN + "╠════════════════════════════════════════════════════════════╣" + RESET);
            System.out.println(CYAN + "║" + BOLD + YELLOW + "  🎖 NEW ACHIEVEMENTS UNLOCKED:                              " + CYAN + "║" + RESET);
            String[] badges = badgesLog.toString().split("\n");
            for (String b : badges) {
                System.out.printf(CYAN + "║%-60s" + CYAN + "║\n" + RESET, b);
            }
        }
        
        System.out.println(CYAN + "╚════════════════════════════════════════════════════════════╝" + RESET);
    }
}
