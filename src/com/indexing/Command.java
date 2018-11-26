package com.indexing;

public class Command {
    public int type;
    public String  [] args = null;

    public Command(){}

    public Command(int n) {
        this.type = n;
    }

    public Command(int n, String [] args){
        this.type = n;
        this.args = args;
    }
}
