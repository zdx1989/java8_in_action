package chap6;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collector.Characteristics.CONCURRENT;
import static java.util.stream.Collector.Characteristics.IDENTITY_FINISH;

public class ToListCollector<T> implements Collector<T, List<T>, List<T>> {

    @Override
    public Supplier<List<T>> supplier() {
        return ArrayList::new;
    }

    @Override
    public BiConsumer<List<T>, T> accumulator() {
        return List::add;
    }

    @Override
    public BinaryOperator<List<T>> combiner() {
        return (l1, l2) -> {
            l1.addAll(l2);
            return l1;
        };
    }

    @Override
    public Function<List<T>, List<T>> finisher() {
        return Function.identity();
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Collections.unmodifiableSet(EnumSet.of(CONCURRENT, IDENTITY_FINISH));
    }

    public static void main(String[] args) {
        Stream<String> names = Stream.of("zdx", "ygy", "xx");
        List<String> nameList = names.collect(new ToListCollector<>());
        System.out.println(nameList);

        Stream<Integer> nums = Stream.of(1, 2, 3, 4);
        List<Integer> numList = nums.collect(ArrayList::new, List::add, List::addAll);
        System.out.println(numList);
    }
}
