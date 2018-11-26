package com.indexing;

import java.nio.file.Path;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

public class CSVReader {

    public boolean hasNext = false;

    private String delim;
    private BufferedReader br = null;
    private String currentLine;
    private int cnt = 0;

    public CSVReader(String filePath, String delim) throws IOException {
        this.delim = delim;

        this.br = new BufferedReader(new FileReader(filePath));

        this.moveForvard();
    }

    public String [] nextLine() throws IOException {
        String [] res;
        if(this.hasNext){
            res = this.currentLine.split(this.delim);
//            System.out.println("Returning array of len: " + res.length)
        } else {
            // This should not happen
            res  = new String[1];
        }

        this.moveForvard();

        return res;
    }

    private void moveForvard() throws IOException {
        this.currentLine = br.readLine();

        if(this.currentLine != null){
            this.hasNext = true;
        } else {
            this.hasNext = false;
        }
    }

}
