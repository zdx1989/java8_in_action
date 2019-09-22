package chap8;

public class SpellCheckProcessing extends ProcessObject<String> {

    @Override
    String handleWork(String input) {
        return input == null ? null : input.replaceAll("labda", "lambda");
    }
}
