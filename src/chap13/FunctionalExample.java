
 package chap13;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.LongStream;

import static java.util.stream.Collectors.*;

 public class FunctionalExample {

    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1, 4, 9);
        System.out.println(subsets(list));
 }

    private static List<List<Integer>> subsets(List<Integer> list) {
        if (list.isEmpty()) {
            List<List<Integer>> ans = new ArrayList<>();
            ans.add(Collections.emptyList());
            return ans;
        } else {
            Integer first = list.get(0);
            List<Integer> rest = list.subList(1, list.size());
            List<List<Integer>> subAns = subsets(rest);
            List<List<Integer>> subAns2 = insertAll(first, subAns);
            return concat(subAns, subAns2);
        }
    }

    private static List<List<Integer>> insertAll(Integer first, List<List<Integer>> subAns) {
        return subAns.stream()
                .map(l -> {
                    List<Integer> ans = new ArrayList<>();
                    ans.add(first);
                    ans.addAll(l);
                    return ans;
                })
                .collect(toList());
    }

    private static List<List<Integer>> concat(List<List<Integer>> subAns1, List<List<Integer>> subAns2) {
        List<List<Integer>> ans = new ArrayList<>();
        ans.addAll(subAns1);
        ans.addAll(subAns2);
        return ans;
    }

    private static long factorialRecursive(long n) {
        return n == 1 ? n : n * factorialRecursive(n - 1);
    }

    private static long factorialStream(long n) {
        return LongStream.rangeClosed(1, n)
                .reduce(1, (x, y) -> x * y);
    }

    private static long factorialTailRecursive(long n) {
        return factorialHelper(1, n);
    }

    private static long factorialHelper(long acc, long n) {
        return n == 1? acc: factorialHelper(acc * n, n - 1);
    }
}
