package main;

import controller.GameController;
import controller.MainMenuController;
import model.Boxer;
import model.Enemy;
import model.MenuState;
import view.GameView;
import view.MainMenuView;

// Próximo ejecutable
public class Main {
    public static void main(String[] args) {
        MenuState menuState = MenuState.MENU;

        while (menuState != MenuState.EXIT) {
            MainMenuView menuView = new MainMenuView();
            MainMenuController menuController = new MainMenuController(menuView);
            menuState = menuController.showMenu();

            if (menuState == MenuState.START_GAME) {
                Boxer boxer = new Boxer("Jugador", 100, 10, 150, 100);
                Enemy enemy = new Enemy(100, 20, 200, 200); // <-- crea el enemigo aquí
                GameView gameView = new GameView(boxer, enemy); // <-- pásalo aquí
                GameController gameController = new GameController(gameView, boxer, enemy); 
                gameController.startGame();
                gameController.waitForGameEnd(); // Espera hasta que el jugador salga del juego
                menuState = MenuState.MENU;
            }
        }
        System.exit(0);
    }
}