package reqandres;

import java.util.ArrayList;
import model.GameData;

public record ListGamesResult(ArrayList<GameData> games, String message) {}
