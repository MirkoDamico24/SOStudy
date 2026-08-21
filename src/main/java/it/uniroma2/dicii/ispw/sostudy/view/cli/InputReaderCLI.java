package it.uniroma2.dicii.ispw.sostudy.view.cli;

import java.util.Scanner;

public class InputReaderCLI {
    private InputReaderCLI() {} // Evita l'istanziazione

    public static int readInteger(String prompt, String errorMsg, Scanner scanner) {
        int result = 0;
        boolean valid = false;
        while (!valid) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                result = Integer.parseInt(input);
                valid = true;
            } catch (NumberFormatException e) {
                System.out.println(errorMsg);
            }
        }
        return result;
    }
}