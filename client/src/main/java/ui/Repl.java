package ui;

import java.util.Scanner;
import static ui.EscapeSequences.*;

public class Repl {
    private final ChessClient client;
    private State state;

    public static void main(String[] args) {
        System.out.println("this is hopefully a chessboard at some point.");
    }

    public Repl(String serverUrl) {
        client = new ChessClient(serverUrl);
        state = client.getState();
    }

    public void run() {
        System.out.println(WHITE_KING + " Welcome to 240 Chess. Type help to get Started." + BLACK_QUEEN);

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                state = client.getState();
                System.out.print(SET_TEXT_COLOR_BLUE + result);
            }
            catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        String stateMessage = "";
        if (state == State.SIGNEDOUT) {
            stateMessage = "[LOGGED_OUT]";
        }
        else if (state == State.SIGNEDIN) {
            stateMessage = "[LOGGED_IN]";
        }
        else if (state == State.PLAYINGGAME) {
            stateMessage = "[PLAYING]";
        }
        System.out.print("\n" + RESET_TEXT_COLOR + stateMessage + " >>> " + SET_TEXT_COLOR_GREEN);
    }
}
