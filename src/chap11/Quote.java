package chap11;

public class Quote {

    private final String showName;
    private final double price;
    private final Discount.Code discountCode;

    public Quote(String showName, double price, Discount.Code discountCode) {
        this.showName = showName;
        this.price = price;
        this.discountCode = discountCode;
    }

    public String getShowName() {
        return showName;
    }

    public double getPrice() {
        return price;
    }

    public Discount.Code getDiscountCode() {
        return discountCode;
    }

    public static Quote parse(String s) {
        String[] split = s.split(":");
        String shopName = split[0];
        double price = Double.parseDouble(split[1]);
        Discount.Code discountCode = Discount.Code.valueOf(split[2]);
        return new Quote(shopName, price, discountCode);
    }
}
