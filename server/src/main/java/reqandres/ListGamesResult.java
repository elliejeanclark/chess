package reqandres;

import java.util.HashMap;
import java.util.ArrayList;
import model.GameData;

public record ListGamesResult(int statusCode, HashMap<String, ArrayList<GameData>> games) {}
