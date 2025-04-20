public class Fibonacii_Number {

    public static void main(String[] args) {
        FibonacciCalculator calculator = new FibonacciCalculator();
        long sum = calculator.cal(100);
        System.out.println("Sum of first 100 even Fibonacci numbers: " + sum);
    }
}

class FibonacciCalculator {

    public long cal(int count) {
        long sum = 0;
        long a = 0, b = 2; 
        int found = 0;

        while (found < count) {
            sum += b;
            found++;

            // Generate next even Fibonacci number using the formula:
            // E(n) = 4 * E(n-1) + E(n-2)
            long next = 4 * b + a;
            a = b;
            b = next;
        }

        return sum;
    }
}