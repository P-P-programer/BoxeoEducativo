package main;

import controller.GameController;
import view.GameView;
import model.Boxer;

public class Main {
    public static void main(String[] args) {
        // Crea el modelo del boxeador (nombre, salud, fuerza, posición inicial x, y)
        Boxer boxer = new Boxer("Jugador", 100, 10, 150, 100);

        // Crea la vista y le pasa el modelo
        GameView gameView = new GameView(boxer);

        // Crea el controlador y le pasa la vista y el modelo
        GameController gameController = new GameController(gameView, boxer);

        // Inicia el juego
        gameController.startGame();
    }
}