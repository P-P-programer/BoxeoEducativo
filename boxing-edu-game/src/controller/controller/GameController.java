package controller;

import model.GameState;
import view.GameView;

public class GameController {
    private GameState gameState;
    private GameView gameView;

    public GameController(GameView gameView) {
        this.gameView = gameView;
        this.gameState = new GameState();
    }

    public void startGame() {
        gameState.resetGame();
        gameView.initializeUI();
        updateGameState();
    }

    public void handleUserInput(String input) {
        // Logic to handle user input and update game state
        // For example, process attack or defend actions
    }

    public void updateGameState() {
        // Logic to update the game state based on current conditions
        // This could include updating scores, checking for knockouts, etc.
    }
}