package chap6;

import chap4.CaloriesLevel;
import chap4.Dish;
import chap5.Trader;
import chap5.Transaction;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.*;

public class UseCollectors {

    public static Optional<Dish> getMaxCaloriesDish(List<Dish> menu) {
        return menu.stream()
                .max(comparingInt(Dish::getCalories));
    }

    public static int getSumCaloriesDish(List<Dish> menu) {
        return menu.stream()
                .collect(Collectors.summingInt(Dish::getCalories));
    }

    public static double getAvgCaloriesDish(List<Dish> menu) {
        return menu.stream()
                .collect(Collectors.averagingInt(Dish::getCalories));
    }

    public static Optional<Dish> getMostCaloriesDish(List<Dish> menu) {
        return menu.stream()
                .collect(Collectors.reducing(
                        (a, b) -> a.getCalories() > b.getCalories() ? a : b));
    }

    public static int getTotalCalories(List<Dish> menu) {
        return menu.stream()
                .collect(Collectors.reducing(0, Dish::getCalories, Integer::sum));
    }

    public static <T> Collector<T, ?, Long> counting() {
        return Collectors.reducing(0L, x -> 1L, Long::sum);
    }

    public static long getTotalDishes(List<Dish> menu) {
        return menu.stream()
                .collect(counting());
    }

    public static Map<Dish.Type, List<Dish>> getDishesByType(List<Dish> menu) {
        return menu.stream()
                .collect(groupingBy(Dish::getType));
    }

    public static Map<CaloriesLevel, List<Dish>> getDishesByCaloriesLevel(List<Dish> menu) {
        return menu.stream()
                .collect(groupingBy(classifyDishesByCaloriesLevel()));
    }

    private static Function<Dish, CaloriesLevel> classifyDishesByCaloriesLevel() {
        return dish -> {
            int calories = dish.getCalories();
            if (calories <= 400) return CaloriesLevel.DIET;
            else if (calories <= 700) return CaloriesLevel.NORMAL;
            else return CaloriesLevel.FAT;
        };
    }

    public static Map<Dish.Type, Map<CaloriesLevel, List<Dish>>> getDishesByTypeCaloriesLevel(List<Dish> menu) {
        return menu.stream()
                .collect(groupingBy(Dish::getType,
                            groupingBy(classifyDishesByCaloriesLevel())
                        )
                );
    }

    public static Map<Dish.Type, Long> getTypesCount(List<Dish> menu) {
        return menu.stream()
                .collect(groupingBy(Dish::getType, counting()));
    }

    public static Map<Dish.Type, Optional<Dish>> getMostCaloriesDish1(List<Dish> menu) {
        return menu.stream()
                .collect(groupingBy(Dish::getType, maxBy(comparingInt(Dish::getCalories))));
    }

    public static Map<Dish.Type, Dish> getMostCaloriesByType(List<Dish> menu) {
        return menu.stream()
            .collect(
                groupingBy(Dish::getType,
                    collectingAndThen(
                            maxBy(comparingInt(Dish::getCalories)),
                            Optional::get
                        )
                    )
            );
    }

    public static Map<Dish.Type, Set<CaloriesLevel>> getCaloriesLevelByType(List<Dish> menu) {
        return menu.stream()
                .collect(groupingBy(Dish::getType, mapping(classifyDishesByCaloriesLevel(), toSet())));
    }

    public static Map<Boolean, List<Dish>> getVegetarianDishes(List<Dish> menu) {
        return menu.stream()
                .collect(partitioningBy(Dish::isVegetarian));
    }

    public static Map<Boolean, Map<Dish.Type, List<Dish>>> getVegetarianDishByType(List<Dish> menu) {
        return menu.stream()
                .collect(
                    partitioningBy(
                        Dish::isVegetarian,
                        groupingBy(Dish::getType)
                    )
                );
    }

    public static Map<Boolean, Dish> getMostCaloriesByVegetarian(List<Dish> menu) {
        return menu.stream()
                .collect(partitioningBy(
                    Dish::isVegetarian,
                    collectingAndThen(
                        maxBy(comparingInt(Dish::getCalories)),
                        Optional::get)
                    )
                );
    }

    public static boolean isPrime(int num) {
        int root = (int) Math.sqrt(num);
        return IntStream.rangeClosed(2, root)
                .noneMatch(i -> num % i == 0);
    }

    public static Map<Boolean, List<Integer>> getPartitionPrimes(int n) {
        return IntStream.rangeClosed(2, n)
                .boxed()
                .collect(partitioningBy(UseCollectors::isPrime));
    }

    public static Map<Boolean, List<Integer>> partitionPrimesWithCustomCollector(int n) {
        return IntStream.rangeClosed(2, n)
                .boxed()
                .collect(new PrimeNumbersCollector());
    }

    public static void main(String[] args) {
        Trader raoul = new Trader("Raoul", "Cambridge");
        Trader mario = new Trader("Mario","Milan");
        Trader alan = new Trader("Alan","Cambridge");
        Trader brian = new Trader("Brian","Cambridge");
        List<Transaction> transactions = Arrays.asList(
                new Transaction(brian, 2011, 300),
                new Transaction(raoul, 2012, 1000),
                new Transaction(raoul, 2011, 400),
                new Transaction(mario, 2012, 710),
                new Transaction(mario, 2012, 700),
                new Transaction(alan, 2012, 950)
        );

        List<Dish> menu = Arrays.asList(
                new Dish("pork", false, 800, Dish.Type.MEAT),
                new Dish("beef", false, 700, Dish.Type.MEAT),
                new Dish("chicken", false, 400, Dish.Type.MEAT),
                new Dish("french fries", true, 530, Dish.Type.OTHER),
                new Dish("rice", true, 350, Dish.Type.OTHER),
                new Dish("season fruit", true, 120, Dish.Type.OTHER),
                new Dish("pizza", true, 550, Dish.Type.OTHER),
                new Dish("prawns", false, 300, Dish.Type.FISH),
                new Dish("salmon", false, 450, Dish.Type.FISH)
        );

        Map<Dish.Type, List<Dish>> dishesByTypes = getDishesByType(menu);
        System.out.println(dishesByTypes);

        Map<CaloriesLevel, List<Dish>> dishesByCalories = getDishesByCaloriesLevel(menu);
        System.out.println(dishesByCalories);

        Map<Dish.Type, Map<CaloriesLevel, List<Dish>>> dishesByTypeCaloriesLevel = getDishesByTypeCaloriesLevel(menu);
        System.out.println(dishesByTypeCaloriesLevel);

        Map<Dish.Type, Long> dishesTypesCount = getTypesCount(menu);
        System.out.println(dishesTypesCount);

        Map<Dish.Type, Optional<Dish>> dishesTypesMaxCalories = getMostCaloriesDish1(menu);
        System.out.println(dishesTypesMaxCalories);

        Map<Dish.Type, Dish> mostCaloriesDish = getMostCaloriesByType(menu);
        System.out.println(mostCaloriesDish);

        Map<Dish.Type, Set<CaloriesLevel>> dishesTypeCaloriesLevel = getCaloriesLevelByType(menu);
        System.out.println(dishesTypeCaloriesLevel);

        Map<Boolean, List<Dish>> vegetarianDishes = getVegetarianDishes(menu);
        System.out.println(vegetarianDishes);

        Map<Boolean, Map<Dish.Type, List<Dish>>> vegetarianDishByType = getVegetarianDishByType(menu);
        System.out.println(vegetarianDishByType);

        Map<Boolean, Dish> mostCaloriesByVegetarian = getMostCaloriesByVegetarian(menu);
        System.out.println(mostCaloriesByVegetarian);

        Map<Boolean, List<Integer>> partitionPrime = getPartitionPrimes(50);
        System.out.println(partitionPrime);

        Map<Boolean, List<Integer>> partitionPrimes = partitionPrimesWithCustomCollector(50);
        System.out.println(partitionPrimes);
    }
}
