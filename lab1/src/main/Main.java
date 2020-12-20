package main;

import java.io.IOException;
import application.Application;

public class Main {
    public static void main(String[] args) {
        var application = Application.getInstance();
        try {
            application.init(args[0], args[1]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}