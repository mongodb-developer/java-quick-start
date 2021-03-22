package com.mongodb.quickstart.csfle;

public class ConsoleDecoration {

    public static void printSection(String msg) {
        StringBuilder starsBuilder = new StringBuilder();
        for (int i = 0; i < msg.length() + 4; i++) {
            starsBuilder.append("*");
        }
        String stars = starsBuilder.toString();
        System.out.println("\n" + stars);
        System.out.println("* " + msg + " *");
        System.out.println(stars);
    }

}
