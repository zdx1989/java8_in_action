package chap8;

public class ICBC extends OnLineBanking{

    @Override
    protected void makeCustomerHappy(String username) {
        System.out.println(username + ", Thanks for using ICBC");
    }


}
