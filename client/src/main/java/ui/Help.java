package ui;

public class Help {
    private State state;

    public Help(State state) {
        this.state = state;
    }

    public String getHelp() {
        if (state == State.SIGNEDOUT) {
            return """
                    login <USERNAME> <PASSWORD> - to login and play chess
                    register <USERNAME> <PASSWORD> <EMAIL> - to create a new account
                    quit - exit the program
                    help - list possible commands
                    """;
        }
        else if (state == State.SIGNEDIN) {
            return """
                    create <NAME> - create a game
                    list - list all games
                    join <ID> [WHITE|BLACK] - join a game based on ID and pick a color
                    observe <id> - watch a game
                    logout - logout of the system
                    quit - exit the program
                    help - list possible commands
                    """;
        }
        else if (state == State.PLAYINGGAME) {
            return """
                    redraw - redraws the chess board to its current state.
                    leave - leave the game.
                    move <row>,<col> <row>,<col> <optional promotion piece> - move the piece at the first index to the second index
                    resign - forfeit the game.
                    legal <row> <col> - allows you to see legal moves for a piece at the given index.
                    quit - exit the program
                    help - list possible commands
                    """;
        }
        else {
            return """
                    exit - exit watch/playing mode.
                    quit - quit the program.
                    help - list possible commands.
                    """;
        }
    }
}
