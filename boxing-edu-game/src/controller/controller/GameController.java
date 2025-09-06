package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.Timer;

import model.Boxer;
import model.GameState;
import view.GameView;

public class GameController {
	private GameState gameState;
	private GameView gameView;
	private Boxer boxer;
	private Timer moveTimer;
	private Set<String> teclasPresionadas = new HashSet<>();

	public GameController(GameView gameView, Boxer boxer) {
		this.gameView = gameView;
		this.boxer = boxer;
		this.gameState = new GameState();
		configurarControles();
		configurarTimerMovimiento();
	}

	public void startGame() {
		gameState.resetGame();
		gameView.initializeUI();
		moveTimer.start();
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
		moveTimer = new Timer(100, new ActionListener() { // 100 ms entre movimientos
			@Override
			public void actionPerformed(ActionEvent e) {
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
				gameView.actualizarVista();
			}
		});
	}

	public void updateGameState() {
		// Aquí puedes actualizar lógica adicional del juego
	}
}