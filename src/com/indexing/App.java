package com.indexing;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;


/**
 * Top level logic of the application. While loop waiting for users commands
 */
public class App {
    public String idp = "C:\\Users\\pavel\\Skola\\InformationRetrieval\\Index";
    public String businessesFilePath = "C:\\Users\\pavel\\Skola\\InformationRetrieval\\business.json";
    public String reviewFilePath = "C:\\Users\\pavel\\Skola\\InformationRetrieval\\review.json";
    public FSDirectory index;

    public void run() throws IOException, ParseException {
        DataAdapter da = new DataAdapter(businessesFilePath, reviewFilePath);
        ReviewIndexer ri = new ReviewIndexer(idp, da);

        TextUI ui = new TextUI();

        boolean exit = false;
        while(!exit) {
            Command cmd = ui.getCommand();
            switch (cmd.type) {
                case -1:
                    System.out.println("Not a valid command!");
                    break;
                case 0:
                    break;
                case 1:
                    this.index = createIdx(ri, cmd);
                    if(this.index == null) System.out.println("NULLLLLLL");
                    break;
                case 2:
                    this.cleanDir();
                    break;
                case 3:
                    exit = true;
                    break;

                case 4:
                    this.performQuery(cmd);
                    break;

            }

        }
    }

    public FSDirectory createIdx(ReviewIndexer indexer, Command cmd) throws IOException {
        if(this.index != null) {
                this.index.close();
        }

        cleanDir();

        int limit = 10000;
        if(cmd.args != null){
            limit = Integer.parseInt(cmd.args[0]);
        }

        System.out.println("\tCreating index with first " + limit + " files...");
        Long l = System.nanoTime();
        FSDirectory d = indexer.createIndex(limit);
        float f = (System.nanoTime() - l)/1000000000F;
        System.out.println("\tDone in " + f + " seconds.");
        return d;

    }

    public void cleanDir(){
        //        Clean the directory for index
        try {
            FileUtils.cleanDirectory(new File(idp));
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("\tDirectory cleaned");
    }

    public void performQuery(Command cmd) throws ParseException, IOException {
        StandardAnalyzer analyzer = new StandardAnalyzer();

        String s1 = "pizza";
        String s2 = "It was a really good pizza";
        String s3 = "Best place in Pickering to go if your looking for some pizza. Service is quick and the pizza is reasonably priced. The pizza here is better tasting than it's alternatives Little Caesars and Pizza Pizza. The staff are a bit creepy but it doesn't matter because it only takes a minute to order a pizza.";
        Query q;
        if(cmd.args == null)
             q = new QueryParser("reviewText", analyzer).parse(s2);
        else
             q = new QueryParser("reviewText", analyzer).parse(cmd.args[0]);

        IndexReader reader = DirectoryReader.open(this.index);
        IndexSearcher searcher = new IndexSearcher(reader);

        Long l = System.nanoTime();
        TopDocs docs = searcher.search(q, 3);
        float f = (System.nanoTime() - l)/1000000000F;
        System.out.println("\tDone in " + f + " seconds.");
        ScoreDoc[] hits = docs.scoreDocs;

        if(hits.length == 0){
            System.out.println("No docs found");
            return;
        }
        for(int i=0; i < hits.length; ++i) {
            int docId = hits[i].doc;
            Document d = searcher.doc(docId);
            System.out.println((i + 1) + ". " + d.get("businessName") + "\t" + d.get("reviewText"));
        }

        reader.close();
    }
}
