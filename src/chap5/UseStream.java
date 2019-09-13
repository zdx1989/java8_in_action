package chap5;

import chap4.Dish;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Comparator.*;
import static java.util.stream.Collectors.*;

public class UseStream {

    public static List<Dish> getVegetarianMenu(List<Dish> menu) {
        return menu.stream()
                .filter(Dish::isVegetarian)
                .collect(toList());
    }

    public static List<Integer> getEvenNumbers(List<Integer> numbers) {
        return numbers.stream()
                .filter(n -> n % 2 == 0)
                .distinct()
                .collect(toList());
    }

    public static List<Dish> skipHighCaloriesMenu(List<Dish> menu, int calories, int n) {
        return menu.stream()
                .filter(d -> d.getCalories() > calories)
                .skip(n)
                .collect(toList());
    }

    public static List<Integer> getDishNameLen(List<Dish> menu) {
        return menu.stream()
                .map(Dish::getName)
                .map(String::length)
                .collect(toList());
    }

    public static List<String> getUniqueCharacters(List<String> words) {
        return words.stream()
                .map(w -> w.split(""))
                .flatMap(Arrays::stream)
                .distinct()
                .collect(toList());
    }

    public static Optional<Dish> findVegetarianDish(List<Dish> menu) {
        return find(menu.stream(), Dish::isVegetarian);
    }

    private static <T> Optional<T> find(Stream<T> stream, Predicate<T> predicate) {
        return stream
                .filter(predicate)
                .findFirst();
    }

    public static long getUniqueWordsCount(String filePath) {
        try (Stream<String> lines = Files.lines(Paths.get(filePath), Charset.defaultCharset())) {
            return lines
                    .flatMap(l -> Arrays.stream(l.split("")))
                    .distinct()
                    .count();
        } catch (IOException e) {
            return 0;
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
        List<Dish> vegetarianMenu = getVegetarianMenu(menu);
        System.out.println(vegetarianMenu);

        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 4, 6, 7, 2);
        List<Integer> evenNumbers = getEvenNumbers(numbers);
        System.out.println(evenNumbers);

        List<Dish> highCaloriesMenu = skipHighCaloriesMenu(menu, 300, 2);
        System.out.println(highCaloriesMenu);

        List<Integer> dishNameLen = getDishNameLen(menu);
        System.out.println(dishNameLen);

        List<String> worlds = Arrays.asList("Hello", "world");
        List<String> uniqueCharacters = getUniqueCharacters(worlds);
        System.out.println(uniqueCharacters);

        findVegetarianDish(menu)
                .ifPresent(System.out::println);

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

        List<Transaction> tr2011 =
                transactions.stream()
                .filter(t -> t.getYear()== 2011)
                .sorted(comparing(Transaction::getValue))
                .collect(toList());
        System.out.println(tr2011);

        List<String> cities =
                transactions.stream()
                .map(t -> t.getTrader().getCity())
                .distinct()
                .collect(toList());
        System.out.println(cities);

        List<Trader> traders =
                transactions.stream()
                .filter(d -> d.getTrader().getCity().equals("Cambridge"))
                .map(Transaction::getTrader)
                .sorted(comparing(Trader::getName))
                .collect(toList());
        System.out.println(traders);

        String traderStr =
                transactions.stream()
                .map(d -> d.getTrader().getName())
                .distinct()
                .sorted()
                .collect(joining());
        System.out.println("traderStr: " + traderStr);

        boolean milanBase =
                transactions.stream()
                .anyMatch(d -> d.getTrader().getCity().equals("Milan"));
        System.out.println("Milan base: " + milanBase);

        transactions.stream()
                .filter(d -> "Cambridge".equals(d.getTrader().getCity()))
                .map(Transaction::getValue)
                .forEach(System.out::println);

        OptionalInt max =
                transactions.stream()
                .mapToInt(Transaction::getValue)
                .max();
        max.ifPresent(System.out::println);

        Optional<Transaction> min =
                transactions.stream()
                .min(comparing(Transaction::getValue));
        min.ifPresent(System.out::println);

        List<Integer> evenNum =
                IntStream.rangeClosed(1, 100)
                .filter(n -> n % 2 == 0)
                .boxed()
                .collect(toList());
        System.out.println(evenNum);

        Stream.generate(Math::random)
                .limit(5)
                .forEach(System.out::println);

    }
}
