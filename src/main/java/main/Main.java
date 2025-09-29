package main;

import controller.GameController;
import controller.MainMenuController;
import model.Boxer;
import model.Enemy;
import model.MenuState;
import view.GameView;
import view.MainMenuView;

import java.util.ArrayList;
import java.util.List;

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
                Enemy enemy = new Enemy(100, 20, 200, 200);

                // Crea la lista de enemigos y agrega el inicial
                List<Enemy> enemies = new ArrayList<>();
                enemies.add(enemy);

                // Pasa la lista al GameView
                GameView gameView = new GameView(boxer, enemies);

                // Pasa la lista al GameController (ajusta el constructor si es necesario)
                GameController gameController = new GameController(gameView, boxer, enemies);

                gameController.startGame();
                gameController.waitForGameEnd();
                menuState = MenuState.MENU;
            }
        }
        System.exit(0);
    }
}