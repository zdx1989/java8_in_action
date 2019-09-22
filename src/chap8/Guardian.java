package chap8;

public class Guardian implements Observer {

    @Override
    public void notify(String tweet) {
        if (tweet != null && tweet.contains("queen")) {
            System.out.println("Yet another news in Landon..." + tweet);
        }
    }
}
