package main;

import controller.GameController;
import view.GameView;

public class Main {
    public static void main(String[] args) {
        GameView gameView = new GameView();
        GameController gameController = new GameController(gameView);
        gameController.startGame(); // Esto mostrará la ventana Swing
    }
}