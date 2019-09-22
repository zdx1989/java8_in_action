package chap8;

import chap4.CaloriesLevel;
import chap4.Dish;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.List;
import java.util.Map;
import java.util.function.*;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.*;

public class LambdaExample {

    // lambda表达式替换匿名内部类
    public static void main(String[] args) {
        Runnable r1 = new Runnable() {
            @Override
            public void run() {
                System.out.println("hello");
            }
        };

        Runnable r2 = () -> System.out.println("hello");

        //对lambda进行显示的类型转换
        doSomething(r1);
        doSomething((Task) () -> System.out.println("Hello"));

        //对于复杂的lambda表达式使用方法引用来代替

        //从命令式的数据处理，切换到stream处理

        //行为参数化

        //有条件的延时执行

        //环绕执行

        //使用lambda重构面向对象的设计模式

        //替换策略模式
        Validator lowerCaseValidator = new Validator(new IsAllowLowerCase());
        Boolean bl1 = lowerCaseValidator.validate("zdx");
        Validator numberValidator = new Validator(new IsNumeric());
        Boolean bl2 = numberValidator.validate("123");

        Validator lowerCaseValidator1 = new Validator(s -> s.matches("[a-z]+"));
        Validator numberValidator1 = new Validator(s -> s.matches("\\d+"));

        //Validator类真的有必要存在吗？使用Predicate建模完全可以满足
        Predicate<String> lowerCaseValidator2 = s -> s.matches("[a-z]+");
        Predicate<String> numberValidator2 = s -> s.matches("\\d+");
        Predicate<String> lowerCaseAndNumValidator = lowerCaseValidator2.and(numberValidator2);
        boolean bl3 = lowerCaseValidator2.test("zdx");
        boolean bl4 = numberValidator2.test("123");
        //逻辑上永远为false
        boolean b15 = lowerCaseAndNumValidator.test("zz");

        //替换模板方法
        new ICBC().processCustomer(123);
        new OnlineBank().processCustomer1(123, username -> System.out.println(username + ", Thanks for using ICBC"));
        //模板方法真的有必要吗？
        IntConsumer ICBC = onlineBank(username -> System.out.println(username + ", Thanks for using ICBC"));
        ICBC.accept(123);

        //替换观察者模式
        //主题 --> 状态发生改变 --> 自动通知其他的观察者
        //观察者接口 Observer
        //主题接口 Subject
        Feed feed = new Feed();
        feed.registerObserver(new NYTimes());
        feed.registerObserver(new Guardian());
        feed.registerObserver(new LeMonde());
        feed.notifyObservers("The queen said her favourite book is java8 in action");

        feed.registerObserver(tweet -> {
            if (tweet != null && tweet.contains("queen")) {
                System.out.println("Yet anther news in London..." + tweet);
            }
        });

        //责任链模式
        //创建处理对象链条的通用方案
        //一个对象处理完工作之后再传递给下一个对象， 下一个对象做类似的工作，以此类推
        ProcessObject<String> p1 = new HeaderTextProcessing();
        ProcessObject<String> p2 = new SpellCheckProcessing();
        p1.setSuccessor(p2);
        String res = p1.handle("Aren't labdas really sexy?");
        System.out.println(res);

        //lambda的组合
        UnaryOperator<String> headerTextProcess = s -> "From zdx: " + s;
        UnaryOperator<String> spellCheckerProcess = s -> s.replaceAll("labda", "lambda");
        Function<String, String> p11 = headerTextProcess.andThen(spellCheckerProcess);
        String res1 = p11.apply("Aren't labdas really sexy?");
        System.out.println(res1);

        //工厂模式
        //String -> 不同product的子类

    }

    public static void doSomething(Runnable r) {
        r.run();
    }

    public static void doSomething(Task t) {
        t.execute();
    }

    public static Map<CaloriesLevel, List<Dish>> dishesByCaloriesLLevel(List<Dish> menu) {
        return menu.stream()
                .collect(Collectors.groupingBy(d -> {
                    int calories = d.getCalories();
                    if (calories < 400) return CaloriesLevel.DIET;
                    else if (calories < 700) return CaloriesLevel.NORMAL;
                    else return CaloriesLevel.FAT;
                }));
    }

    public static Map<CaloriesLevel, List<Dish>> dishesByCaloriesLevel(List<Dish> menu) {
        return menu.stream()
                .collect(groupingBy(Dish::getCaloriesLevel));
    }

    public static IntConsumer onlineBank(Consumer<String> consumer) {
        return id -> {
            String username = id + "_account";
            consumer.accept(username);
        };
    }
}
