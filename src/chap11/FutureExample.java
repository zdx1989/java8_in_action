package chap11;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

import static java.util.stream.Collectors.toList;

public class FutureExample {

    private static final Executor executor = Executors.newFixedThreadPool(4,
        (Runnable r) -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        }
    );


    public static void main(String[] args) {
//        ExecutorService executorService = Executors.newCachedThreadPool();
//        Future<Double> future = executorService.submit(FutureExample::doSomeLongComputation);
//        doSomethingElse();
//        try {
//            Double result = future.get(1, TimeUnit.SECONDS);
//        } catch (InterruptedException e) {
//            //当前线程在等待的过程中被中断
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            //计算抛出异常
//            e.printStackTrace();
//        } catch (TimeoutException e) {
//            //Future计算完成前超时
//            e.printStackTrace();
//        }

        List<Shop> shops = Arrays.asList(
                new Shop("BestPrice"),
                new Shop("LetsSaveBig"),
                new Shop("MyFavoriteShop"),
                new Shop("BuyItAll")
        );


//        long start = System.nanoTime();
//        System.out.println(getPrices(shops, "HuaWei Mate30"));
//        long duration = (System.nanoTime() - start) / 1_000_000;
//        System.out.println("Done in " + duration + " msecs");
//
//        long start1 = System.nanoTime();
//        System.out.println(getPricePar(shops, "HuaWei Mate30"));
//        long duration1 = (System.nanoTime() - start1) / 1_000_000;
//        System.out.println("Done in " + duration1 + " msecs");


        long start2 = System.nanoTime();
        System.out.println(findPrice(shops, "HuaWei Mate30"));
        long duration2 = (System.nanoTime() - start2) / 1_000_000;
        System.out.println("Done in " + duration2 + " msecs");

        long start3 = System.nanoTime();
        System.out.println(findPrice1(shops, "HuaWei Mate30"));
        long duration3 = (System.nanoTime() - start3) / 1_000_000;
        System.out.println("Done in " + duration3 + " msecs");

        long start4 = System.nanoTime();
        System.out.println(findPrice2(shops, "HuaWei Mate30"));
        long duration4 = (System.nanoTime() - start4) / 1_000_000;
        System.out.println("Done in " + duration4 + " msecs");

    }
    public static Double doSomeLongComputation() throws InterruptedException {
        Thread.sleep(500);
        return 100D;
    }

    public static void doSomethingElse() {
        System.out.println("this is done");
    }

    public static List<String> getPrices(List<Shop> shops, String product) {
        List<CompletableFuture<String>> priceFutures = shops.stream()
                .map(shop -> CompletableFuture.supplyAsync(
                        () -> String.format("%s price is %.2f", shop.getName(), shop.getPrice(product)),
                        generateExecutor(shops.size())))
                .collect(toList());

        return priceFutures.stream()
                .map(CompletableFuture::join)
                .collect(toList());
    }

    public static List<String> getPricePar(List<Shop> shops, String product) {
        return shops.parallelStream()
                .map(shop -> String.format("%s price is %.2f", shop.getName(), shop.getPrice(product)))
                .collect(toList());
    }

    public static Executor generateExecutor(int count) {
        return Executors.newFixedThreadPool(Math.min(count, 100),
                (Runnable r) -> {
                        Thread t = new Thread(r);
                        t.setDaemon(true);
                        return t;
                    }
                );
    }

    public static List<String> findPrice(List<Shop> shops, String product) {
        return shops.stream()
                .map(shop -> shop.getPrice1(product))
                .map(Quote::parse)
                .map(Discount::applyDiscount)
                .collect(toList());
    }

    //thenCompose 将多个CompleteFuture组合，前后存在依赖，Monad ?
    public static List<String> findPrice1(List<Shop> shops, String product) {
        List<CompletableFuture<String>> priceFutures =
                shops.stream()
                .map(shop -> CompletableFuture.supplyAsync(
                        () -> shop.getPrice1(product), generateExecutor(shops.size())
                ))
                .map(future -> future.thenApply(Quote::parse))
                .map(future -> future.thenCompose(quote ->
                        CompletableFuture.supplyAsync(
                                () -> Discount.applyDiscount(quote), generateExecutor(shops.size()))
                ))
                .collect(toList());
        return priceFutures.stream()
                .map(CompletableFuture::join)
                .collect(toList());
    }

    //thenCombine 将多个CompleteFuture组合，前后不存在依赖，Applicative?
    public static List<Double> findPrice2(List<Shop> shops, String product) {
        List<CompletableFuture<Double>> pricesFuture =
                shops.stream()
                .map(shop -> CompletableFuture.supplyAsync(() -> shop.getPrice(product)))
                .map(future -> future.thenCombine(
                        CompletableFuture.supplyAsync(FutureExample::getRate),
                        (price, rate) -> price * rate
                ))
                .collect(toList());
        return pricesFuture.stream()
                .map(CompletableFuture::join)
                .collect(toList());
    }

    //使用Java7的API完成函数
//    public static List<Double> findPrice3(List<Shop> shops, String product) {
//        Future<Double> futurePrices = executor
//    }

    private static double getRate() {
        delay();
        return 1.2;
    }

    private static void delay() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }
    }
}
