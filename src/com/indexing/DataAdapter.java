package com.indexing;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.io.BufferedReader;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Reads data from yelp dataset files, aggregates them and transforms them into instances of Review class
 */
public class DataAdapter implements IDataAdapter {

    private Map<String, String> idToName = new HashMap<String, String>();
    private BufferedReader brRev = null;
    private String reviewsFilePath;
    private String currentLine;

    private boolean hasNext = false;

    public DataAdapter(String businessesFilePath, String reviewsFilePath){

        this.reviewsFilePath = reviewsFilePath;

        try {
            BufferedReader br = new BufferedReader(new FileReader(businessesFilePath));
            String line, id, name;
            while ((line = br.readLine()) != null) {
                JsonObject jsonObject = new JsonParser().parse(line).getAsJsonObject();
                id = jsonObject.get("business_id").getAsString();
                name = jsonObject.get("name").getAsString();
                this.idToName.put(id, name);
            }
            //this.reset();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

    }

    public Review getNextReview() throws IOException {
        Review res = new Review();
        JsonObject jsonObject = new JsonParser().parse(this.currentLine).getAsJsonObject();

        res.id = jsonObject.get("review_id").getAsString();
        res.business = this.idToName.get( jsonObject.get("business_id").getAsString() );
        res.text = jsonObject.get("text").getAsString();
        res.useful = jsonObject.get("useful").getAsString();

        this.moveToNext();
        return res;
    }

    public boolean hasNext(){
        return this.hasNext;
    }

    private void moveToNext() throws IOException {
        this.currentLine = this.brRev.readLine();
        if(this.currentLine == null){
            this.hasNext = false;
        } else {
            this.hasNext = true;
        }
    }

    public void close() throws IOException {
        //this.brRev.close();
    }

    public void reset() throws IOException {
        this.brRev = new BufferedReader(new FileReader(this.reviewsFilePath));
        this.moveToNext();
    }
}
