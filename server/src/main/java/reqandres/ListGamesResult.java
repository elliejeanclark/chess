package reqandres;

import java.util.ArrayList;
import model.GameData;

public record ListGamesResult(int StatusCode, ArrayList<GameData> games) {}
