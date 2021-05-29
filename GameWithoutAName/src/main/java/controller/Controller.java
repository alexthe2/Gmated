package controller;

import lombok.Getter;

public class Controller {
    @Getter
    private static int currentPlayer;

    public Controller() {
        currentPlayer = 0;
    }

    public static void move() {
        currentPlayer = 1 - currentPlayer;
    }
}
