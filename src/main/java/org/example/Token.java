package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * загружаем токен бота из файла
 */
public class Token {
    String token = "";
    public String get() {
        return token;
    }
    public void load() {
        String fileName = "src/main/resources/tocken.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            token = reader.readLine();
            System.out.println("Токен загружен: " + token);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
