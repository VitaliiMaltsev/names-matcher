import matcher.Aggregator;
import matcher.Matcher;
import matcher.NameLocation;
import matcher.dataprovider.UrlDataProvider;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Main {

    public static void main(String[] args) {
        try {
            final URL url = new URL("http://norvig.com/big.txt");

            Matcher matcher = new Matcher(new UrlDataProvider(url));
            final List<Map<String, List<NameLocation>>> fullMatchesList = matcher.findMatches();

            Aggregator aggregator = new Aggregator(fullMatchesList);
            Map<String, List<NameLocation>> nameLocationsMap = aggregator.aggregate();
            aggregator.printResult(nameLocationsMap);

        } catch (InterruptedException e) {
            // TODO log different exceptions
            e.printStackTrace();
        } catch (ExecutionException e2) {
            e2.printStackTrace();
        } catch (IOException e4) {
            e4.printStackTrace();
        }
    }
}