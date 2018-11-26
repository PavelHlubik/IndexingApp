package com.indexing;

/**
 * Represents one document (e.g. review
 */
public class Review {
    public String id = null;
    public String business = null;
    public String text = null;
    public String useful = null;

    public int sizeOf(){
        int n = 0;
        if(this.id != null)
            n += this.id.length();

        if(this.business != null)
            n += this.business.length();

        if(this.text != null)
            n += this.text.length();

        if(this.useful != null)
            n += this.useful.length();
        return n;
    }
}
