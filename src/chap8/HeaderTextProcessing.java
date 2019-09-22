package chap8;

public class HeaderTextProcessing extends ProcessObject<String> {

    @Override
    String handleWork(String input) {
        return "From zdx: " + input;
    }
}
