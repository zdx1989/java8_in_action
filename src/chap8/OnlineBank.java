package chap8;

import java.util.function.Consumer;

public class OnlineBank {

    public void processCustomer1(int id, Consumer<String> consumer) {
        String username = id + "_account";
        consumer.accept(username);
    }
}
