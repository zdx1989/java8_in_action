package chap4;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.Comparator.*;
import static java.util.stream.Collectors.toList;

public class Dishes {

    public static final Comparator<Dish> comparator = comparing(Dish::getCalories);

    public static Stream<String> findDishes(List<Dish> menu, Predicate<Dish> predicate, Comparator<Dish> comparator) {
        return menu.stream()
                .filter(predicate)
                .sorted(comparator)
                .map(Dish::getName);
    }

    public static List<String> findLowCaloriesDishes(List<Dish> menu, int lowCalories) {
        return findDishes(menu, d -> d.getCalories() < lowCalories, comparator)
                .collect(toList());
    }

    public static List<String> findHighCaloriesDish(List<Dish> menu, int highCalories) {
        return findDishes(menu, d -> d.getCalories() > highCalories, comparator.reversed())
                .collect(toList());
    }

    public static List<String> topNCaloriesDish(List<Dish> menu, int n) {
        return findDishes(menu, d -> true, comparator.reversed())
                .limit(3)
                .collect(toList());
    }

    public static <T> void println(T... list) {
        for (T t: list) {
            System.out.println(t);
        }
    }

    public static void main(String[] args) {
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
        List<String> lowCaloriesDishes = findLowCaloriesDishes(menu, 400);
        List<String> highCaloriesDishes = findHighCaloriesDish(menu, 400);
        println(lowCaloriesDishes);
        println();
        println(highCaloriesDishes);

        List<String> topNCaloriesDishes = topNCaloriesDish(menu, 3);
        println(topNCaloriesDishes);
    }

}
