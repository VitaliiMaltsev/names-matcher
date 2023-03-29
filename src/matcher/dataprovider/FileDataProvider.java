package matcher.dataprovider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class FileDataProvider implements DataProvider {
    private final InputStream inputStream;

    public FileDataProvider(String path) throws FileNotFoundException {
        File file = new File(path);
        inputStream = new FileInputStream(file);
    }

    public InputStream getStream() {
        return inputStream;
    }
}