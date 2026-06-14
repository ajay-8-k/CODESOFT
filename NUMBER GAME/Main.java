import java.util.List;
import java.util.Scanner;

/**
 * Main coordinator class for "Mystery Number Challenge Pro".
 * Manages game configuration, menus, statistics dashboard, and handles user inputs.
 */
public class Main {

    // ANSI Color Codes
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String MAGENTA = "\u001B[35m";
    private static final String CYAN = "\u001B[36m";
    private static final String BOLD = "\u001B[1m";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ScoreBoard scoreBoard = new ScoreBoard();

        displayWelcomeBanner();
        
        System.out.print(BOLD + "👤 Enter your name: " + RESET);
        String name = scanner.nextLine();
        Player player = new Player(name);

        System.out.println("\n" + GREEN + "Welcome, " + BOLD + player.getName() + RESET + "! Let's crack the code.");
        
        boolean exit = false;
        while (!exit) {
            displayMainMenu();
            System.out.print(BOLD + "Selection > " + RESET);
            String selection = scanner.nextLine().trim();

            switch (selection) {
                case "1":
                    handleGameRounds(scanner, player, scoreBoard);
                    break;
                case "2":
                    displayStatistics(player);
                    break;
                case "3":
                    scoreBoard.displayLeaderboard();
                    break;
                case "4":
                    exit = true;
                    break;
                default:
                    System.out.println(RED + "⚠ INVALID OPTION: Please enter a choice between 1 and 4." + RESET);
                    break;
            }
        }

        displayGoodbyeBanner();
        scanner.close();
    }

    /**
     * Prints the retro-cyberpunk styled title screen ASCII banner.
     */
    private static void displayWelcomeBanner() {
        System.out.println(CYAN + "=========================================================================" + RESET);
        System.out.println(BOLD + MAGENTA + 
            "  __  __           _                    _   _                 _             \n" +
            " |  \\/  |_   _ ___| |_ ___ _ __ _   _  | \\ | |_   _ _ __ ___ | |__   ___ _ __ \n" +
            " | |\\/| | | | / __| __/ _ \\ '__| | | | |  \\| | | | | '_ ` _ \\| '_ \\ / _ \\ '__|\n" +
            " | |  | | |_| \\__ \\ ||  __/ |  | |_| | | |\\  | |_| | | | | | | |_) |  __/ |   \n" +
            " |_|  |_|\\__, |___/\\__\\___|_|   \\__, | |_| \\_|\\__,_|_| |_| |_|_.__/ \\___|_|   \n" +
            "         |___/                  |___/                                        " + RESET);
        System.out.println(BOLD + CYAN + "                       ★ MYSTERY NUMBER CHALLENGE PRO ★                      " + RESET);
        System.out.println(CYAN + "=========================================================================" + RESET);
    }

    /**
     * Prints the primary console menu using a double-bordered panel.
     */
    private static void displayMainMenu() {
        System.out.println("\n" + CYAN + "╔════════════════════════════════════════════════════════════╗" + RESET);
        System.out.println(CYAN + "║" + BOLD + YELLOW + "                      MAIN MENU                             " + CYAN + "║" + RESET);
        System.out.println(CYAN + "╠════════════════════════════════════════════════════════════╣" + RESET);
        System.out.println(CYAN + "║ " + RESET + "  1. 🎮 Start Game Challenge                                " + CYAN + "║" + RESET);
        System.out.println(CYAN + "║ " + RESET + "  2. 📊 View Lifetime Statistics                            " + CYAN + "║" + RESET);
        System.out.println(CYAN + "║ " + RESET + "  3. 🏆 View High Scores Leaderboard                         " + CYAN + "║" + RESET);
        System.out.println(CYAN + "║ " + RESET + "  4. 🚪 Exit Program                                        " + CYAN + "║" + RESET);
        System.out.println(CYAN + "╚════════════════════════════════════════════════════════════╝" + RESET);
    }

    /**
     * Loops rounds of game difficulty selection and play sessions.
     */
    private static void handleGameRounds(Scanner scanner, Player player, ScoreBoard scoreBoard) {
        boolean keepPlaying = true;

        while (keepPlaying) {
            Game.Difficulty difficulty = promptDifficulty(scanner);
            if (difficulty == null) {
                // User chose to return to Main Menu
                return;
            }

            // Play the game
            Game game = new Game(player, difficulty);
            int roundScore = game.play(scanner);

            // Add score to leaderboard if the player won (score > 0)
            if (roundScore > 0) {
                scoreBoard.addScore(player.getName(), roundScore);
            }

            // Loop to handle "Play Again" response
            while (true) {
                System.out.print("\n🔁 Would you like to play another round? (Y/N): ");
                String playAgainInput = scanner.nextLine().trim().toUpperCase();
                
                if (playAgainInput.equals("Y") || playAgainInput.equals("YES")) {
                    break; // break the inner loop, continue playing
                } else if (playAgainInput.equals("N") || playAgainInput.equals("NO")) {
                    keepPlaying = false;
                    break;
                } else {
                    System.out.println(RED + "⚠ INVALID SELECTION: Please type 'Y' (Yes) or 'N' (No)." + RESET);
                }
            }
        }
    }

    /**
     * Visual UI sub-menu to select difficulty levels.
     */
    private static Game.Difficulty promptDifficulty(Scanner scanner) {
        while (true) {
            System.out.println("\n" + CYAN + "╔════════════════════════════════════════════════════════════╗" + RESET);
            System.out.println(CYAN + "║" + BOLD + MAGENTA + "                  SELECT DIFFICULTY LEVEL                   " + CYAN + "║" + RESET);
            System.out.println(CYAN + "╠════════════════════════════════════════════════════════════╣" + RESET);
            System.out.println(CYAN + "║ " + RESET + "  1. 🟢 Easy   (Range: 1-50  | Attempts: 15)                  " + CYAN + "║" + RESET);
            System.out.println(CYAN + "║ " + RESET + "  2. 🟡 Medium (Range: 1-100 | Attempts: 10)                  " + CYAN + "║" + RESET);
            System.out.println(CYAN + "║ " + RESET + "  3. 🔴 Hard   (Range: 1-500 | Attempts: 8)                   " + CYAN + "║" + RESET);
            System.out.println(CYAN + "║ " + RESET + "  4. 🔙 Back to Main Menu                                     " + CYAN + "║" + RESET);
            System.out.println(CYAN + "╚════════════════════════════════════════════════════════════╝" + RESET);
            System.out.print(BOLD + "Selection > " + RESET);
            
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    return Game.Difficulty.EASY;
                case "2":
                    return Game.Difficulty.MEDIUM;
                case "3":
                    return Game.Difficulty.HARD;
                case "4":
                    return null;
                default:
                    System.out.println(RED + "⚠ INVALID OPTION: Please enter a choice between 1 and 4." + RESET);
            }
        }
    }

    /**
     * Renders a dashboard card containing the player's lifetime accomplishments.
     */
    private static void displayStatistics(Player player) {
        System.out.println("\n" + CYAN + "╔════════════════════════════════════════════════════════════╗" + RESET);
        System.out.println(CYAN + "║" + BOLD + YELLOW + "                  📊 PLAYER PERFORMANCE CARD                 " + CYAN + "║" + RESET);
        System.out.println(CYAN + "╠════════════════════════════════════════════════════════════╣" + RESET);
        System.out.printf(CYAN + "║  " + RESET + "Player Profile: " + BOLD + CYAN + "%-38s" + CYAN + "║\n" + RESET, player.getName());
        System.out.printf(CYAN + "║  " + RESET + "Total Games Played: " + BOLD + "%-33d" + CYAN + "║\n" + RESET, player.getTotalGamesPlayed());
        System.out.printf(CYAN + "║  " + RESET + "Total Victories: " + BOLD + GREEN + "%-35d" + CYAN + "║\n" + RESET, player.getGamesWon());
        System.out.printf(CYAN + "║  " + RESET + "Win Rate Percentage: " + BOLD + YELLOW + "%-31.2f%%" + CYAN + "║\n" + RESET, player.getWinRate());
        System.out.printf(CYAN + "║  " + RESET + "Highest Score Registered: " + BOLD + GREEN + "%-26d pts" + CYAN + "║\n" + RESET, player.getHighestScore());
        System.out.printf(CYAN + "║  " + RESET + "Average Winning Attempts: " + BOLD + "%-30.1f" + CYAN + "║\n" + RESET, player.getAverageAttempts());
        System.out.println(CYAN + "╠════════════════════════════════════════════════════════════╣" + RESET);
        System.out.println(CYAN + "║" + BOLD + YELLOW + "  🏅 EARNED BADGES / ACCOMPLISHMENTS:                       " + CYAN + "║" + RESET);
        
        List<String> badges = player.getEarnedBadges();
        if (badges.isEmpty()) {
            System.out.printf(CYAN + "║ %-56s   " + CYAN + "║\n" + RESET, "  None yet. Unlocked badges will display here!");
        } else {
            for (String badge : badges) {
                String icon = "⭐";
                if (badge.contains("Sharp")) icon = "🎯";
                if (badge.contains("Persistent")) icon = "🛡️";
                System.out.printf(CYAN + "║   " + GREEN + "%s %-52s " + CYAN + "║\n" + RESET, icon, badge);
            }
        }
        System.out.println(CYAN + "╚════════════════════════════════════════════════════════════╝" + RESET);
    }

    /**
     * Prints an ASCII art sign-off banner on program termination.
     */
    private static void displayGoodbyeBanner() {
        System.out.println("\n" + CYAN + "=========================================================================" + RESET);
        System.out.println(BOLD + MAGENTA +
            "  ⚡ Shutting down core engines...\n" +
            "  👋 Thank you for playing Mystery Number Challenge Pro!\n" +
            "  🔒 Goodbye, Agent! Keep codebreaking." + RESET);
        System.out.println(CYAN + "=========================================================================" + RESET);
    }
}
