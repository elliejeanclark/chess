package reqandres;

import java.util.HashMap;
import java.util.ArrayList;
import model.GameData;

public record ListGamesResult(int StatusCode, HashMap<String, ArrayList<GameData>> games) {}
