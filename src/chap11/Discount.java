package chap11;

public class Discount {

    public enum Code {
        NONE(0), SILVER(5), GOLD(10), PLATINUM(15), DIAMOND(20);

        private final int percentage;

        Code(int percentage) {
            this.percentage = percentage;
        }
    }

    public static String applyDiscount(Quote quote) {
        return quote.getShowName() + " price is " + apply(quote.getPrice(), quote.getDiscountCode());
    }

    private static double apply(double price, Code discountCode) {
        delay();
        return price * (100 - discountCode.percentage) / 100;
    }

    private static void delay() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }
    }
}
