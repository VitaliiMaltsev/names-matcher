package matcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Aggregator {
    private final List<Map<String, List<NameLocation>>> fullMatchesList;

    public Aggregator(List<Map<String, List<NameLocation>>> fullMatchesList) {
        this.fullMatchesList = fullMatchesList;
    }

    public Map<String, List<NameLocation>> aggregate() {
        Map<String, List<NameLocation>> word2locations = new HashMap<>();
        fullMatchesList.forEach(chunkWord2locations ->
                chunkWord2locations.forEach((word, locations) ->
                        word2locations.computeIfAbsent(word, k -> new ArrayList<>()).addAll(locations)));
        return word2locations;
    }

    public void printResult(Map<String, List<NameLocation>> nameLocationsMap){
        nameLocationsMap.forEach((name, locations) -> {
            System.out.println(name + " --> " + locations);
        });
    }
}
