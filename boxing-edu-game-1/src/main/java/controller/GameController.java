package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.Timer;

import model.Boxer;
import model.GameState;
import view.GameView;

//controlador genral del juego

public class GameController {
	private GameState gameState;
	private GameView gameView;
	private Boxer boxer;
	private Timer moveTimer;
	private Set<String> teclasPresionadas = new HashSet<>();
	private CountDownLatch latch;

	private int enemigoAnimFrame = 0;
	private int enemigoFrameCount = 9; // igual que frameCount en GameView

	public GameController(GameView gameView, Boxer boxer) {
		this.gameView = gameView;
		this.boxer = boxer;
		this.gameState = new GameState();
		this.latch = new CountDownLatch(1);
		configurarControles();
		configurarTimerMovimiento();

		// Agrega un botón de salir en la ventana del juego
		if (gameView instanceof GameView) {
			gameView.addSalirListener(() -> {
				gameView.closeGame();
				latch.countDown();
			});
		}
	}

	public void startGame() {
		gameState.resetGame();
		gameView.initializeUI();
		moveTimer.start();
	}

	public void waitForGameEnd() {
		try {
			latch.await();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	private void configurarControles() {
		// Key bindings para detectar presionar y soltar teclas
		InputMap inputMap = gameView.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap actionMap = gameView.getActionMap();

		inputMap.put(KeyStroke.getKeyStroke("pressed W"), "presionarArriba");
		inputMap.put(KeyStroke.getKeyStroke("released W"), "soltarArriba");
		inputMap.put(KeyStroke.getKeyStroke("pressed S"), "presionarAbajo");
		inputMap.put(KeyStroke.getKeyStroke("released S"), "soltarAbajo");
		inputMap.put(KeyStroke.getKeyStroke("pressed A"), "presionarIzquierda");
		inputMap.put(KeyStroke.getKeyStroke("released A"), "soltarIzquierda");
		inputMap.put(KeyStroke.getKeyStroke("pressed D"), "presionarDerecha");
		inputMap.put(KeyStroke.getKeyStroke("released D"), "soltarDerecha");

		actionMap.put("presionarArriba", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				teclasPresionadas.add("W");
			}
		});
		actionMap.put("soltarArriba", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				teclasPresionadas.remove("W");
			}
		});
		actionMap.put("presionarAbajo", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				teclasPresionadas.add("S");
			}
		});
		actionMap.put("soltarAbajo", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				teclasPresionadas.remove("S");
			}
		});
		actionMap.put("presionarIzquierda", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				teclasPresionadas.add("A");
			}
		});
		actionMap.put("soltarIzquierda", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				teclasPresionadas.remove("A");
			}
		});
		actionMap.put("presionarDerecha", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				teclasPresionadas.add("D");
			}
		});
		actionMap.put("soltarDerecha", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				teclasPresionadas.remove("D");
			}
		});
	}

	private void configurarTimerMovimiento() {
		moveTimer = new Timer(100, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Movimiento del jugador
				boolean seMovio = false;
				if (teclasPresionadas.contains("W")) {
					boxer.move(0, -10);
					seMovio = true;
				}
				if (teclasPresionadas.contains("S")) {
					boxer.move(0, 10);
					seMovio = true;
				}
				if (teclasPresionadas.contains("A")) {
					boxer.move(-10, 0);
					seMovio = true;
				}
				if (teclasPresionadas.contains("D")) {
					boxer.move(10, 0);
					seMovio = true;
				}
				if (!seMovio) {
					boxer.resetAnimation();
				}

				// Movimiento del enemigo hacia el jugador
				int enemigoX = gameView.getEnemigoX();
				int enemigoY = gameView.getEnemigoY();
				int jugadorX = boxer.getX();
				int jugadorY = boxer.getY();

				int dx = jugadorX - enemigoX;
				int dy = jugadorY - enemigoY;
				int step = 6; // velocidad del enemigo

				int dir = 2; // por defecto abajo

				if (Math.abs(dx) > 2 || Math.abs(dy) > 2) { // Solo mueve si está lejos
					if (Math.abs(dx) > Math.abs(dy)) {
						// Se mueve más en X
						if (dx > 0) {
							enemigoX += step;
							dir = 3; // derecha
						} else if (dx < 0) {
							enemigoX -= step;
							dir = 1; // izquierda
						}
					} else {
						// Se mueve más en Y
						if (dy > 0) {
							enemigoY += step;
							dir = 2; // abajo
						} else if (dy < 0) {
							enemigoY -= step;
							dir = 0; // arriba
						}
					}
					enemigoAnimFrame = (enemigoAnimFrame + 1) % enemigoFrameCount;
				} else {
					// Si ya está sobre el jugador, no anima ni mueve
					enemigoAnimFrame = 0;
				}

				gameView.setEnemigoPosition(enemigoX, enemigoY);
				gameView.setEnemigoAnimation(dir, enemigoAnimFrame);

				gameView.actualizarVista();
			}
		});
	}

	public void updateGameState() {
		// Aquí puedes actualizar lógica adicional del juego
	}
}