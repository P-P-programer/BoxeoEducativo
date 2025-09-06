package main;

import controller.GameController;
import controller.MainMenuController;
import model.Boxer;
import model.MenuState;
import view.GameView;
import view.MainMenuView;

public class Main {
    public static void main(String[] args) {
        MenuState menuState = MenuState.MENU;

        while (menuState != MenuState.EXIT) {
            MainMenuView menuView = new MainMenuView();
            MainMenuController menuController = new MainMenuController(menuView);
            menuState = menuController.showMenu();

            if (menuState == MenuState.START_GAME) {
                // Inicializa las estadisticas del personaje
                Boxer boxer = new Boxer("Jugador", 100, 10, 150, 100);
                GameView gameView = new GameView(boxer);
                GameController gameController = new GameController(gameView, boxer);
                gameController.startGame();
                menuState = MenuState.MENU; // Vuelve al menú al terminar el juego
            }
        }
        System.exit(0);
    }
}