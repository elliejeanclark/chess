
import ui.*;

public class Main {
    public static void main(String[] args) {
        String url = "http://localhost:8080";
        Repl repl = new Repl(url);
        repl.run();
    }
}