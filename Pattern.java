import java.util.Scanner;

public class Pattern {

    public static int calculatePatternSum(int x) {

        if (x < 0 || x > 9) {
            throw new IllegalArgumentException("Input must be a single digit (0–9).");
        }

        int total = 0;
        int term = 0;

        for (int i = 0; i < 4; i++) {
            term = term * 10 + x;
            total += term;
        }

        return total;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.print("Enter a single digit (0–9): ");
            int x = scanner.nextInt();

            int result = calculatePatternSum(x);
            System.out.println("Result: " + result);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Invalid input. Please enter a number.");
        }

        scanner.close();
    }
}
