package chap7;

import java.util.concurrent.ForkJoinPool;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static java.util.Comparator.comparing;

public class ParallelStream {

    public static void main(String[] args) {
        long n = 10_000_000;
//        println(ParallelStream::sequentialSum, n);
//        println(ParallelStream::sequentialParSum, n);
//        println(ParallelStream::iterativeSum, n);
        println(ParallelStream::rangeSum, n);
        println(ParallelStream::sideEffectSum, n);
        println(ParallelStream::forkJoinSum, n);
        final String SENTENCE =
                " Nel mezzo del cammin di nostra vita " +
                        "mi ritrovai in una selva oscura" +
                        " ché la dritta via era smarrita ";
        System.out.println("Found " + countWordsIteratively(SENTENCE) + " words");
    }

    public static long sequentialSum(long n) {
        return LongStream.iterate(1L, i -> i + 1)
                .limit(n)
                .sum();
    }

    public static long sequentialParSum(long n) {
        return LongStream.iterate(1, i -> i + 1)
                .limit(n)
                .parallel()
                .sum();
    }

    public static long iterativeSum(long n) {
        long result = 0;
        for (long i = 1; i <= n; i++) {
            result += i;
        }
        return result;
    }

    public static long rangeSum(long n) {
        return LongStream.rangeClosed(1, n)
                .parallel()
                .sum();
    }

    public static long sideEffectSum(long n) {
        Accumulator accumulator = new Accumulator();
        LongStream.rangeClosed(1, n)
                .parallel()
                .forEach(accumulator::add);
        return accumulator.total;
    }

    public static long forkJoinSum(long n) {
        long[] numbers = LongStream.rangeClosed(1, n).toArray();
        ForkJoinSumCalculator forkJoinSumCalculator = new ForkJoinSumCalculator(numbers);
        return new ForkJoinPool().invoke(forkJoinSumCalculator);
    }

    public static int countWordsIteratively(String s) {
        int count = 0;
        boolean lastSpace = true;
        for (char c: s.toCharArray()) {
            if (Character.isWhitespace(c)) {
                lastSpace = true;
            } else {
                if (lastSpace) count++;
                lastSpace = false;
            }
        }
        return count;
    }

    public static <T, R> void println(Function<T, R> function, T t) {
        Object[] res =
            IntStream.range(0, 10)
                    .boxed()
                    .map(i -> {
                        long begin = System.currentTimeMillis();
                        R r = function.apply(t);
                        long end = System.currentTimeMillis();
                        return new Object[]{end - begin, r};
                    })
                    .max(comparing(arr -> (Long)arr[0]))
                    .get();
        System.out.println("result: " + res[1]);
        System.out.println("cost: " + res[0] + " ms");
    }
}
