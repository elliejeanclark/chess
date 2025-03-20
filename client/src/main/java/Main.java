
import ui.*;

public class Main {
    public static void main(String[] args) {

        String url = "http://localhost:8080";
        ChessClient client = new ChessClient(url);
        Repl repl = new Repl(url);
        repl.run();
    }
}