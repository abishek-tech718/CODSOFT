import java.util.Random;
import java.util.Scanner;

public class NumberGame {

    static Scanner scanner = new Scanner(System.in);
    static Random random = new Random();

    static final int MAX_ATTEMPTS = 7;

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════╗");
        System.out.println("║       NUMBER GUESSING GAME       ║");
        System.out.println("╚══════════════════════════════════╝");

        int roundsWon = 0;
        int roundsPlayed = 0;
        int totalAttempts = 0;

        boolean playAgain = true;

        while (playAgain) {
            roundsPlayed++;
            System.out.println("\n--- Round " + roundsPlayed + " ---");

            int[] result = playRound();
            int attemptsUsed = result[0];
            boolean won = result[1] == 1;

            totalAttempts += attemptsUsed;

            if (won) {
                roundsWon++;
                System.out.println("✔ You guessed it in " + attemptsUsed + " attempt(s)!");
            } else {
                System.out.println("✘ Better luck next round!");
            }

            displayScore(roundsPlayed, roundsWon, totalAttempts);

            System.out.print("\nPlay again? (yes/no): ");
            String choice = scanner.nextLine().trim().toLowerCase();
            playAgain = choice.equals("yes") || choice.equals("y");
        }

        System.out.println("\n╔══════════════════════════════════╗");
        System.out.println("║           FINAL SCORE            ║");
        System.out.println("╠══════════════════════════════════╣");
        System.out.printf("║  Rounds Played  : %-14d║%n", roundsPlayed);
        System.out.printf("║  Rounds Won     : %-14d║%n", roundsWon);
        System.out.printf("║  Total Attempts : %-14d║%n", totalAttempts);
        double avg = roundsPlayed > 0 ? (double) totalAttempts / roundsPlayed : 0;
        System.out.printf("║  Avg Attempts   : %-14.1f║%n", avg);
        System.out.println("╚══════════════════════════════════╝");
        System.out.println("Thanks for playing! Goodbye.");
    }

    static int[] playRound() {
        System.out.print("Enter range start (default 1): ");
        int rangeStart = readIntOrDefault(1);

        System.out.print("Enter range end   (default 100): ");
        int rangeEnd = readIntOrDefault(100);

        if (rangeEnd <= rangeStart) {
            System.out.println("Invalid range. Using 1–100.");
            rangeStart = 1;
            rangeEnd = 100;
        }

        int secret = rangeStart + random.nextInt(rangeEnd - rangeStart + 1);

        System.out.println("\nI've picked a number between " + rangeStart
                + " and " + rangeEnd + ".");
        System.out.println("You have " + MAX_ATTEMPTS + " attempts. Good luck!\n");

        for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {
            System.out.print("Attempt " + attempt + "/" + MAX_ATTEMPTS + " → Your guess: ");
            int guess = readIntOrDefault(-1);

            if (guess < rangeStart || guess > rangeEnd) {
                System.out.println("  Please enter a number between "
                        + rangeStart + " and " + rangeEnd + ".");
                attempt--; // don't count invalid input
                continue;
            }

            if (guess == secret) {
                return new int[]{ attempt, 1 };
            } else if (guess < secret) {
                System.out.println("  Too low!  " + remainingHint(attempt));
            } else {
                System.out.println("  Too high! " + remainingHint(attempt));
            }
        }

        System.out.println("Out of attempts! The number was: " + secret);
        return new int[]{ MAX_ATTEMPTS, 0 };
    }

    static String remainingHint(int attempt) {
        int left = MAX_ATTEMPTS - attempt;
        if (left == 0) return "No attempts remaining.";
        return left + " attempt(s) remaining.";
    }

    static void displayScore(int played, int won, int totalAttempts) {
        double winRate = played > 0 ? (double) won / played * 100 : 0;
        System.out.printf("  Score → Rounds: %d | Won: %d | Win Rate: %.0f%%%n",
                played, won, winRate);
    }

    static int readIntOrDefault(int defaultValue) {
        try {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) return defaultValue;
            return Integer.parseInt(line);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}