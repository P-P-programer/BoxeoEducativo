package controller;

import java.util.concurrent.CountDownLatch;

import model.MenuState;
import view.MainMenuView;

public class MainMenuController {
	private MainMenuView menuView;
	private MenuState menuState;

	public MainMenuController(MainMenuView menuView) {
		this.menuView = menuView;
		this.menuState = MenuState.MENU;
	}

	public MenuState showMenu() {
		CountDownLatch latch = new CountDownLatch(1);

		menuView.setStartGameListener(() -> {
			menuState = MenuState.START_GAME;
			menuView.closeMenu();
			latch.countDown();
		});

		menuView.setExitListener(() -> {
			menuState = MenuState.EXIT;
			menuView.closeMenu();
			latch.countDown();
		});

		menuView.initializeMenu();

		try {
			latch.await(); // Espera hasta que el usuario pulse un botón
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

		return menuState;
	}
}