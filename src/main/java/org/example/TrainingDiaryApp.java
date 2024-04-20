package org.example;

import org.example.config.LiquibaseConfig;
import org.example.in.ConsoleUI;

public class TrainingDiaryApp {
    public static void main(String[] args) {
        LiquibaseConfig.runMigrations();
        ConsoleUI consoleUI = new ConsoleUI();
        consoleUI.start();
    }
}