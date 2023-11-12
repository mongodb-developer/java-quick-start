package com.mongodb.quickstart.csfle;

public class ConsoleDecoration {

    public static void printSection(String msg) {
        String stars = "*".repeat(msg.length() + 4);
        System.out.println("\n" + stars);
        System.out.println("* " + msg + " *");
        System.out.println(stars);
    }

}
