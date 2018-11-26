import com.indexing.*;

import java.io.IOException;
import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.queryparser.classic.ParseException;


public class IndexingApp {

    public static void main(String[] args) throws IOException, ParseException {
        App app = new App();
        app.run();
    }


}
