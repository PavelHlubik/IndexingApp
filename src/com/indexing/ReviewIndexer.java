package com.indexing;
import org.apache.lucene.document.*;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import java.math.BigInteger;
import javax.xml.crypto.Data;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Paths;

/**
 * Main Lucene magic is done here.
 * based on: https://www.tutorialspoint.com/lucene/lucene_indexing_process.htm
 */
public class ReviewIndexer {
    private String [] fieldNames;
    private String indexDirPath;
    private IDataAdapter da;

    private int cnt = 0;

    private FSDirectory index;

    /**
     *
     * @param indexDirPath Where to create the index on hard drive
     * @param da instance of DataAdapter, which feeds data for indexing
     */
    public ReviewIndexer(String indexDirPath, IDataAdapter da){
        this.indexDirPath = indexDirPath;
        this.da = da;
    }

    /**
     * Creates the actual index
     * @param limit How many documents to index
     * @return FSDirectory object - created inde
     * @throws IOException
     */
    public FSDirectory createIndex(int limit) throws IOException {

        FSDirectory index = null;

        this.index = FSDirectory.open( Paths.get(this.indexDirPath) );
        StandardAnalyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter writer = new IndexWriter(this.index, config);

        Review r;
        da.reset();
        BigInteger size = BigInteger.ZERO;
        this.cnt = 0;
        while(da.hasNext()){
            r = da.getNextReview();
            size = size.add(BigInteger.valueOf(r.sizeOf()));

            addDocument(writer, r);
            this.cnt++;
            if(this.cnt%10000 == 0){
                System.out.println("\t" + cnt);
            }
            if(cnt >= limit){
                break;
            }
        }

        System.out.printf("\t%d files and %s kB of data indexed\n",cnt,  size.divide(BigInteger.valueOf(1024)).toString());
        this.da.close();
        writer.close();


        return this.index;
    }

    /**
     * Adds a new document into an index that is currently being created
     * @param w IndexWriter - lucene class
     * @param r Instance of Review class - actual data to be indexed
     * @throws IOException
     */
    private void addDocument(IndexWriter w, Review r) throws IOException {
        Document document = new Document();

        //index file contents
        Field idField = new StringField("reviewId", r.id, Field.Store.YES);
        Field businessNameField = new TextField("businessName", r.business, Field.Store.YES);
        Field textField = new TextField("reviewText", r.text, Field.Store.YES);

        Long l =  Long.parseLong(r.useful);
        Field usefulField = new NumericDocValuesField("useful", l);

        document.add(idField);
        document.add(businessNameField);
        document.add(textField);
        document.add(usefulField);

        w.addDocument(document);
    }

}
