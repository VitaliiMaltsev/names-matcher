package matcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.Callable;


public class ChunkMatcher implements Callable<Map<String, List<NameLocation>>> {
    private final String text;
    private final Integer offset;

    public ChunkMatcher(String text, Integer offset) {
        this.text = text;
        this.offset = offset;
    }

    @Override
    public Map<String, List<NameLocation>> call() {
        Map<String, List<NameLocation>> word2locations = new HashMap<>();

        Scanner sc = new Scanner(text);
        int lineOffset = 0;
        while (sc.hasNextLine()) {
            String line = sc.nextLine();

            StringBuilder builder = new StringBuilder();
            int charOffset;
            for (charOffset = 0; charOffset < line.length(); charOffset++) {
                char c = line.charAt(charOffset);

                if (Character.isLetter(c)) {
                    builder.append(c);
                } else {
                    collectMatchedWords(builder, word2locations, lineOffset, charOffset);
                    builder.setLength(0);
                }
            }

            lineOffset++;
        }

        return word2locations;
    }

    private void collectMatchedWords(StringBuilder builder, Map<String, List<NameLocation>> word2locations, int lineOffset, int charOffset) {
        String word = builder.toString();
        if (word.length() > 0 && Constants.NAMES.contains(word)) {
            word2locations.computeIfAbsent(word, k -> new ArrayList<>())
                    .add(new NameLocation(offset + lineOffset + 1, charOffset + 1 - word.length()));
        }
    }
}