package matcher.dataprovider;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class UrlDataProvider implements DataProvider {

    private final InputStream inputStream;

    public UrlDataProvider(URL url) throws IOException {
        this.inputStream = url.openStream();
    }

    @Override
    public InputStream getStream() {
        return inputStream;
    }
}
