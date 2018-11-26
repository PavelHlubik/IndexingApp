package com.indexing;
import  java.util.Scanner;

/**
 * reads commands from input and parses them
 *
 */
public class TextUI {

    public Command getCommand(){
        Scanner lineScanner = new Scanner(System.in);
        String line = lineScanner.nextLine();

        Scanner commandScanner = new Scanner(line);
        String command = null;
        if (commandScanner.hasNext()){
            command = commandScanner.next("[a-z][a-z]*");
        } else{
            return new Command(0);
        }

        System.out.println("command: \"" + command + "\"");

        if(command.equals("create")){
            if(commandScanner.hasNextInt()){
                int n = commandScanner.nextInt();
                String [] s = new String[1];
                s[0] = String.valueOf(n);
                return new Command(1, s);
            } else {
                return new Command(1);
            }
        }
        if(command.equals("drop")){
            if(commandScanner.hasNext(".*")){
                return new Command(-1);
            }
            return new Command(2);

        }

        if(command.equals("exit")) {
            if(commandScanner.hasNext(".*")){
                return new Command(-1);
            }
            return new Command(3);
        }

        if(command.equals("query")) {

            if(commandScanner.hasNext()){
                String queryStr = commandScanner.nextLine();
                String [] s = new String[1];
                s[0] = queryStr;
                return new Command(4, s);
            }

            return new Command(4);
        }

        System.out.println("Not recognized");
        return new Command(-1);
    }
}
