package chap4;

public class Dish {

    private final String name;

    private final boolean vegetarian;

    private final int calories;

    private final Type type;

    public Dish(String name, boolean vegetarian, int calories, Type type) {
        this.name = name;
        this.vegetarian = vegetarian;
        this.calories = calories;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public int getCalories() {
        return calories;
    }

    public boolean isVegetarian() {
        return vegetarian;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return name;
    }

    public enum Type { MEAT, FISH, OTHER }

    public CaloriesLevel getCaloriesLevel() {
        if (calories < 400) return CaloriesLevel.DIET;
        else if (calories < 700) return CaloriesLevel.NORMAL;
        else return CaloriesLevel.FAT;
    }
}
