package matcher;

import matcher.dataprovider.DataProvider;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class Matcher {
    private final DataProvider dataProvider;
    private final List<Future<Map<String, List<NameLocation>>>> futures = new ArrayList<>();
    private final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public Matcher(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    public List<Map<String, List<NameLocation>>> findMatches() throws InterruptedException, ExecutionException {

        try {
            List<Future<Map<String, List<NameLocation>>>> futures = buildFuturesList(dataProvider);
            return futures
                    .stream()
                    .map(future -> {
                        try {
                            return future.get();
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                        throw new RuntimeException("Failed to execute ");
                    }).collect(Collectors.toList());
        } finally {
            executorService.shutdown();
        }
    }


    private void matchWordsInChunk(StringBuilder chunk, Integer chunkNumber) {
        Future<Map<String, List<NameLocation>>> future = executorService.submit(
                new ChunkMatcher(chunk.toString(), chunkNumber * Constants.CHUNK_SIZE));
        futures.add(future);
    }

    private List<Future<Map<String, List<NameLocation>>>> buildFuturesList(DataProvider dataProvider) {
        InputStream inputStream = dataProvider.getStream();
        Scanner sc = new Scanner(inputStream);

        int chunkNumber = 0;
        int lineInChunk = 0;
        StringBuilder builder = new StringBuilder();
        while (sc.hasNextLine()) {
            lineInChunk++;
            String line = sc.nextLine();
            builder.append(line).append(System.lineSeparator());
            if (lineInChunk == Constants.CHUNK_SIZE) {
                matchWordsInChunk(builder, chunkNumber);

                lineInChunk = 0;
                chunkNumber++;
                builder.setLength(0);
            }
        }

        return futures;
    }
}
