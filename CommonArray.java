import java.util.*;

public class CommonElements {
    public static void main(String[] args) {
        int[] arr1 = {1, 2, 4, 5, 6, 8};
        int[] arr2 = {2, 4, 4, 6, 7, 8, 9};

        List<Integer> commonElements = findCommonElements(arr1, arr2);
        System.out.println(commonElements);
    }

    public static List<Integer> findCommonElements(int[] a, int[] b) {
        List<Integer> result = new ArrayList<>();
        int i = 0, j = 0;

        while (i < a.length && j < b.length) {
            if (a[i] == b[j]) {
                if (result.isEmpty() || result.get(result.size() - 1) != a[i]) {
                    result.add(a[i]);
                }
                i++;
                j++;
            } else if (a[i] < b[j]) {
                i++;
            } else {
                j++;
            }
        }

        return result;
    }
}