package Utility;

import java.util.Scanner;

public class Form {
    public static String validId(String welcome) {
        System.out.println(welcome);
        Scanner sc = new Scanner(System.in);
        String id = sc.nextLine();

        while (true) {
            if (id.isEmpty()) {
                System.out.println("ID cannot be empty");
                id = Tools.inputString("Please enter your ID (or others ID ) :   ");
            } else if (id.length() < 3) {
                System.out.println("ID must be at least 3 characters long");
                id = Tools.inputString("Please enter your ID:   ");
            } else {
                return id;
            }
        }
    }

    public static int validYear(String welcome ) {
        int minYear = 2000;
        return Tools.inputInt("Please enter a valid year (greater than 2000): ", minYear, 9999);
    }


}