package com.indexing;

import com.indexing.Review;

import java.io.IOException;

public interface IDataAdapter {
    public Review getNextReview() throws IOException;
    public boolean hasNext();
    public void reset() throws IOException;
    public void close() throws IOException;
}
