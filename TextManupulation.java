import java.io.File;
import java.util.Scanner;
import java.util.Set;
import java.util.HashSet;

public class TextManupulation {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(new File(args[0]));
        Set<String> ips = new HashSet<>();

        while (sc.hasNextLine()) {
            String line = sc.nextLine().replaceAll("[^\\p{Print}\\t]", ""); // Clean line

            String[] parts = line.split(" ");
            if (parts.length > 8 && !parts[8].equals("200")) {
                ips.add(parts[0]); // IP is the first part
            }
        }
        sc.close();

        // Print results
        for (String ip : ips) {
            System.out.println(ip);
        }
    }
}