package chap8;

import java.util.function.Consumer;

public abstract class OnLineBanking {

    public void processCustomer(int id) {
        String username = id + "_account";
        makeCustomerHappy(username);
    }


    protected abstract void makeCustomerHappy(String username);


}
