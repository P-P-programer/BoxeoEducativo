package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import javax.swing.JOptionPane;
import java.awt.Rectangle;

import model.Boxer;
import model.Enemy;
import model.GameState;
import model.PowerUp;
import model.Question;
import model.QuestionBank;
import view.GameView;

import java.util.Random;

/**
 * Controlador general del juego.
 * Maneja el movimiento, ataques, colisiones, animaciones y estado del juego.
 * Futuras implementaciones: bloqueo, K.O., power-ups, IA avanzada, etc.
 */
public class GameController {
    // Estado del juego y referencias a modelos y vista
    private GameState gameState;
    private GameView gameView;
    private Boxer boxer;
    private List<Enemy> enemies = new ArrayList<>();
    private Timer moveTimer;
    private Timer powerUpTimer;
    private Set<String> teclasPresionadas = new HashSet<>();
    private CountDownLatch latch;
    private Random random = new Random();
    private QuestionBank questionBank = new QuestionBank(); // Banco de preguntas
    // Variables de animación y ataque del enemigo
    private int enemigoAnimFrame = 0;
    private int enemigoFrameCount = 9;
    private boolean enemigoAtacando = false;
    private int enemigoAttackFrame = 0;
    private int enemigoAttackFrameCount = 6;
    private int enemigoCooldown = 0;

    // Física básica (para futuras mejoras como salto)
    private static final int GRAVEDAD = 1;
    private static final int SALTO_VEL = -15;

    // Última dirección del boxeador (para animación)
    private String ultimaDireccion = "D";

    /**
     * Constructor principal del controlador.
     */
    public GameController(GameView gameView, Boxer boxer, List<Enemy> enemies) {
        this.gameView = gameView;
        this.boxer = boxer;
        this.enemies = enemies;
        this.gameState = new GameState();
        this.latch = new CountDownLatch(1);

        configurarControles();
        configurarTimerMovimiento();
        configurarTimerPowerUp();

        gameView.setEnemies(enemies);

        // Listener para salir del juego
        gameView.addSalirListener(() -> {
            gameView.closeGame();
            latch.countDown();
        });
    }

    // --- Nuevo: Timer para aparición de PowerUps ---
    private void configurarTimerPowerUp() {
        powerUpTimer = new Timer(10000, new ActionListener() { // 10 segundos
            @Override
            public void actionPerformed(ActionEvent e) {
                int panelWidth = gameView.getWidth();
                int panelHeight = gameView.getHeight();
                double scale = Math.min(panelWidth, panelHeight) / 300.0;

                // Limites lógicos para aparición
                int maxX = (int) (panelWidth / scale) - 32;
                int maxY = gameView.getPowerUpYLimit();
                int x = random.nextInt(Math.max(1, maxX));
                int y = random.nextInt(Math.max(1, maxY));

                PowerUp powerUp = new PowerUp(x, y);
                gameView.addPowerUp(powerUp);
                gameView.actualizarVista();
            }
        });
    }

    /**
     * Inicia el juego y la interfaz.
     */
    public void startGame() {
        gameState.resetGame();
        gameView.initializeUI();
        moveTimer.start();
        powerUpTimer.start(); // <-- Inicia el timer de PowerUps
    }

    /**
     * Espera a que termine la partida (bloqueante).
     */
    public void waitForGameEnd() {
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Configura los controles del teclado para el juego.
     * Futuro: agregar teclas para bloquear, habilidades, etc.
     */
    private void configurarControles() {
        InputMap inputMap = gameView.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = gameView.getActionMap();

        inputMap.put(KeyStroke.getKeyStroke("pressed SPACE"), "saltar");
        inputMap.put(KeyStroke.getKeyStroke("released SPACE"), "soltarSaltar");
        inputMap.put(KeyStroke.getKeyStroke("pressed S"), "presionarAbajo");
        inputMap.put(KeyStroke.getKeyStroke("released S"), "soltarAbajo");
        inputMap.put(KeyStroke.getKeyStroke("pressed A"), "presionarIzquierda");
        inputMap.put(KeyStroke.getKeyStroke("released A"), "soltarIzquierda");
        inputMap.put(KeyStroke.getKeyStroke("pressed D"), "presionarDerecha");
        inputMap.put(KeyStroke.getKeyStroke("released D"), "soltarDerecha");
        inputMap.put(KeyStroke.getKeyStroke("pressed J"), "attackJ");
        inputMap.put(KeyStroke.getKeyStroke("pressed K"), "attackK");
        inputMap.put(KeyStroke.getKeyStroke("pressed W"), "presionarArriba");
        inputMap.put(KeyStroke.getKeyStroke("released W"), "soltarArriba");
        // Futuro: inputMap.put(KeyStroke.getKeyStroke("pressed L"), "bloquear");

        actionMap.put("saltar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                teclasPresionadas.add("SPACE");
            }
        });
        actionMap.put("soltarSaltar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                teclasPresionadas.remove("SPACE");
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
        actionMap.put("attackJ", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!gameView.isBoxerAttackingJ() && !gameView.isBoxerAttackingK()) {
                    gameView.setBoxerAttackingJ(true);
                    gameView.setBoxerAttackFrame(0);
                }
            }
        });
        actionMap.put("attackK", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!gameView.isBoxerAttackingJ() && !gameView.isBoxerAttackingK()) {
                    gameView.setBoxerAttackingK(true);
                    gameView.setBoxerAttackFrame(0);
                }
            }
        });
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
        // Futuro: actionMap.put("bloquear", ...);
    }

    /**
     * Configura el timer principal del juego.
     * Maneja movimiento, colisiones, animaciones y lógica de fin de juego.
     */
    private void configurarTimerMovimiento() {
        moveTimer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // --- Límites responsivos ---
                int panelWidth = gameView.getWidth();
                int panelHeight = gameView.getHeight();
                double scale = Math.min(panelWidth, panelHeight) / 300.0;
                int spriteSize = (int) (64 * scale);
                int minX = 0;
                int maxX = panelWidth - spriteSize;
                int minY = 0;
                int maxY = panelHeight - spriteSize;

                // --- Movimiento del boxeador (X y Y) ---
                int velocidad = Math.max(2, (int)(10 * scale));
                boolean seMovio = false;
                if (teclasPresionadas.contains("A")) {
                    boxer.setX(Math.max(minX, boxer.getX() - velocidad));
                    seMovio = true;
                    ultimaDireccion = "A";
                }
                if (teclasPresionadas.contains("D")) {
                    boxer.setX(Math.min(maxX, boxer.getX() + velocidad));
                    seMovio = true;
                    ultimaDireccion = "D";
                }
                if (teclasPresionadas.contains("W")) {
                    boxer.setY(Math.max(minY, boxer.getY() - velocidad));
                    seMovio = true;
                    ultimaDireccion = "W";
                }
                if (teclasPresionadas.contains("S")) {
                    boxer.setY(Math.min(maxY, boxer.getY() + velocidad));
                    seMovio = true;
                    ultimaDireccion = "S";
                }
                if (seMovio) {
                    gameView.setBoxerWalkFrame((gameView.getBoxerWalkFrame() + 1) % 2);
                } else {
                    gameView.setBoxerWalkFrame(0);
                }
                gameView.setUltimaDireccion(ultimaDireccion);

                // --- Movimiento enemigo (sigue al jugador en X y Y) ---
                for (Enemy enemy : enemies) {
                    int enemigoX = enemy.getX();
                    int enemigoY = enemy.getY();
                    int jugadorX = boxer.getX();
                    int jugadorY = boxer.getY();
                    int dx = jugadorX - enemigoX;
                    int dy = jugadorY - enemigoY;
                    int step = (int)(6 * scale);

                    // Determina la dirección principal para la animación
                    int dir;
                    if (Math.abs(dx) > Math.abs(dy)) {
                        if (dx > 0) {
                            enemigoX = Math.min(maxX, enemigoX + step);
                            dir = 3; // derecha
                        } else if (dx < 0) {
                            enemigoX = Math.max(minX, enemigoX - step);
                            dir = 1; // izquierda
                        } else {
                            dir = 2; // abajo
                        }
                    } else {
                        if (dy > 0) {
                            enemigoY = Math.min(maxY, enemigoY + step);
                            dir = 2; // abajo
                        } else if (dy < 0) {
                            enemigoY = Math.max(minY, enemigoY - step);
                            dir = 0; // arriba
                        } else {
                            dir = 2; // abajo
                        }
                    }
                    enemy.setDirection(dir);

                    // Actualiza el frame de animación
                    int nextFrame = (enemy.getCurrentFrame() + 1) % enemigoFrameCount;
                    enemy.setCurrentFrame(nextFrame);

                    // --- Ataque enemigo ---
                    int dxAtaque = boxer.getX() - enemy.getX();
                    int dyAtaque = boxer.getY() - enemy.getY();
                    int distancia = (int) Math.hypot(dxAtaque, dyAtaque);
                    int rangoAtaque = 64;

                    if (distancia <= rangoAtaque) {
                        if (enemigoCooldown == 0) {
                            enemigoAtacando = true;
                            enemigoAttackFrame++;
                            if (enemigoAttackFrame >= enemigoAttackFrameCount) {
                                enemigoAttackFrame = 0;
                                enemigoCooldown = 10;
                                if (distancia <= rangoAtaque) {
                                    boxer.recibirDanio(enemy.getStrength());
                                }
                            }
                            gameView.setEnemigoAttackAnimation(enemy.getDirection(), Math.max(0, Math.min(enemigoAttackFrame, enemigoAttackFrameCount - 1)));
                        } else {
                            enemigoCooldown--;
                            gameView.setEnemigoAttackAnimation(enemy.getDirection(), 0);
                        }
                    } else {
                        enemigoAtacando = false;
                        enemigoAttackFrame = 0;
                        gameView.setEnemigoAnimation(enemy.getDirection(), enemigoAnimFrame);
                        enemigoCooldown = 0;
                    }

                    // Actualiza la posición del enemigo en el modelo
                    enemy.setX(enemigoX);
                    enemy.setY(enemigoY);

                    // --- Animaciones y ataques del boxeador ---
                    if (gameView.isBoxerAttackingJ()) {
                        gameView.setBoxerAttackFrame(gameView.getBoxerAttackFrame() + 1);
                        if (gameView.getBoxerAttackFrame() == 4) {
                            Rectangle boxerRect = boxer.getHitbox(spriteSize);
                            for (Enemy enemyIter : enemies) {
                                Rectangle enemyRect = enemyIter.getHitbox(spriteSize);
                                if (boxerRect.intersects(enemyRect)) {
                                    enemyIter.recibirDanio(20);
                                    int retroceso = Math.max(10, spriteSize / 3);
                                    if (boxer.getX() < enemyIter.getX()) {
                                        enemyIter.setX(enemyIter.getX() + retroceso);
                                    } else {
                                        enemyIter.setX(enemyIter.getX() - retroceso);
                                    }
                                }
                            }
                            gameView.setBoxerAttackingJ(false);
                            gameView.setBoxerAttackFrame(0);
                        }
                    } else if (gameView.isBoxerAttackingK()) {
                        gameView.setBoxerAttackFrame(gameView.getBoxerAttackFrame() + 1);
                        if (gameView.getBoxerAttackFrame() == 4) {
                            Rectangle boxerRect = boxer.getHitbox(spriteSize);
                            for (Enemy enemyIter : enemies) {
                                Rectangle enemyRect = enemyIter.getHitbox(spriteSize);
                                if (boxerRect.intersects(enemyRect)) {
                                    enemyIter.recibirDanio(20);
                                    int retroceso = Math.max(10, spriteSize / 3);
                                    if (boxer.getX() < enemyIter.getX()) {
                                        enemyIter.setX(enemyIter.getX() + retroceso);
                                    } else {
                                        enemyIter.setX(enemyIter.getX() - retroceso);
                                    }
                                }
                            }
                            gameView.setBoxerAttackingK(false);
                            gameView.setBoxerAttackFrame(0);
                        }
                    }
                }

                // --- Reinicio de enemigo si su vida llega a 0 ---
                for (int i = 0; i < enemies.size(); i++) {
                    Enemy enemy = enemies.get(i);
                    if (enemy.getHealth() <= 0) {
                        enemies.set(i, new Enemy(100, 20, 200, 200));
                        gameView.setEnemies(enemies); // <-- Actualiza la vista
                    }
                }

                // --- Fin de juego si el boxeador pierde toda la vida ---
                if (boxer.getHealth() <= 0) {
                    moveTimer.stop();
                    JOptionPane.showMessageDialog(gameView, "¡Juego terminado! Volverás al menú.", "Fin del juego",
                            JOptionPane.INFORMATION_MESSAGE);
                    gameView.closeGame();
                    latch.countDown();
                    return;
                }

                // --- Recolección de PowerUps ---
                Rectangle boxerRect = boxer.getHitbox(spriteSize);

                for (PowerUp powerUp : gameView.getPowerUps()) {
                    if (!powerUp.isCollected()) {
                        Rectangle powerUpRect = powerUp.getHitbox(spriteSize / 2); // Tamaño del orbe
                        if (boxerRect.intersects(powerUpRect)) {
                            powerUp.setCollected(true);
                            // Lógica para mostrar pregunta y aplicar efecto
                            mostrarPreguntaYAplicarEfecto();
                        }
                    }
                }

                // Actualizar la vista del juego
                gameView.actualizarVista();
                gameView.setEnemies(enemies);
                enemies.removeIf(Enemy::isKnockedOut);
                gameView.setEnemies(enemies);
            }
        });
    }

    /**
     * Método para futuras actualizaciones del estado del juego.
     * Ejemplo: lógica de bloqueo, K.O., power-ups, etc.
     */
    public void updateGameState() {
        // Futuras implementaciones aquí
    }

    // Si quieres detener el timer al cerrar el juego:
    public void stopGame() {
        moveTimer.stop();
        powerUpTimer.stop();
    }

    private void mostrarPreguntaYAplicarEfecto() {
        Question pregunta = questionBank.getRandomQuestion();
        if (pregunta == null) return;

        int seleccion = gameView.mostrarPregunta(pregunta);

        if (seleccion == pregunta.getCorrectIndex()) {
            // Respuesta correcta: restaura vida
            boxer.curar(15); // Usa el método curar del modelo Boxer
            JOptionPane.showMessageDialog(gameView, "¡Correcto! +15 de vida.", "Poder", JOptionPane.INFORMATION_MESSAGE);
        } else {
            // Respuesta incorrecta: penalización y aparece un nuevo enemigo
            boxer.recibirDanio(15); // Usa el método recibirDanio para restar vida
            Enemy newEnemy = new Enemy(100, 20, random.nextInt(gameView.getWidth()), random.nextInt(gameView.getHeight()));
            enemies.add(newEnemy);
            gameView.setEnemy(newEnemy);
            JOptionPane.showMessageDialog(gameView, "Incorrecto. -15 de vida y aparece un enemigo extra.", "Poder", JOptionPane.WARNING_MESSAGE);
        }
        gameView.actualizarVista();
    }
}