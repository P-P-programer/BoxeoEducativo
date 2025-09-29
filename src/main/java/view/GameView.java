package view;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import model.Boxer;
import model.Enemy;
import java.util.List;
import java.util.ArrayList;
import model.PowerUp;
import model.Question;

/**
 * Vista principal del juego.
 * Dibuja personajes, fondos, barras de vida y gestiona la UI.
 * Futuras mejoras: efectos visuales, animaciones de K.O., bloqueo, etc.
 */
public class GameView extends JPanel {
    // --- Referencias a los modelos ---
    private Boxer boxer;
    // private Enemy enemy;

    // --- Animaciones del boxeador ---
    private int boxerIdleFrame = 0;
    private int boxerWalkFrame = 0;
    private int boxerAttackFrame = 0;
    private boolean boxerAttackingJ = false;
    private boolean boxerAttackingK = false;
    private String ultimaDireccion = "D";

    // --- Sprites y fondos ---
    private BufferedImage[] boxerIdle = new BufferedImage[2];
    private BufferedImage[] boxerWalk = new BufferedImage[2];
    private BufferedImage[] boxerAttackJ = new BufferedImage[4];
    private BufferedImage[] boxerAttackK = new BufferedImage[4];
    private BufferedImage[][] walkAnimations = new BufferedImage[4][9]; // [direction][frame]
    private BufferedImage[][] attackAnimations = new BufferedImage[4][6]; // [direction][frame]
    private BufferedImage[] backgrounds = new BufferedImage[12];
    private BufferedImage currentBackground;

    // --- Animaciones del enemigo ---
    private int enemigoDirection = 2; // 0: arriba, 1: izquierda, 2: abajo, 3: derecha
    private int enemigoFrame = 0;
    private boolean enemigoAtacando = false;

    // --- UI y control ---
    private JFrame frame;
    private Runnable onSalir;

    // --- PowerUps ---
    private List<PowerUp> powerUps = new ArrayList<>();
    private static final int POWERUP_Y_LIMIT = 200; // Límite de altura en coordenadas lógicas

    // --- Enemigos ---
    private List<Enemy> enemies = new ArrayList<>();

    // --- Constructor ---
    public GameView(Boxer boxer, List<Enemy> enemies) {
        this.boxer = boxer;
        this.enemies = enemies;
        loadSprites();
        loadBackgrounds();
        setPreferredSize(new Dimension(400, 300));
        setBackground(Color.WHITE);
    }

    /**
     * Carga los sprites del boxeador y enemigo.
     */
    private void loadSprites() {
        try {
            BufferedImage boxerSheet = ImageIO.read(getClass().getResource("/boxeadores/Char_3_No_Armor.png"));
            for (int i = 0; i < 2; i++) {
                boxerIdle[i] = boxerSheet.getSubimage(i * 64, 0, 64, 64);
                boxerWalk[i] = boxerSheet.getSubimage(i * 64, 64, 64, 64);
            }
            for (int i = 0; i < 4; i++) {
                boxerAttackJ[i] = boxerSheet.getSubimage(i * 64, 128, 64, 64);
                boxerAttackK[i] = boxerSheet.getSubimage((i + 4) * 64, 128, 64, 64);
            }

            BufferedImage spriteSheet = ImageIO.read(getClass().getResource("/enemigos/enemigo1.png"));
            int[] caminarYOffsets = { 512, 576, 640, 704 };
            for (int dir = 0; dir < 4; dir++) {
                for (int frame = 0; frame < 9; frame++) {
                    walkAnimations[dir][frame] = spriteSheet.getSubimage(
                        frame * 64, caminarYOffsets[dir], 64, 64
                    );
                }
            }
            int[] attackYOffsets = { 786, 850, 914, 978 };
            for (int dir = 0; dir < 4; dir++) {
                for (int frame = 0; frame < 6; frame++) {
                    attackAnimations[dir][frame] = spriteSheet.getSubimage(
                        frame * 64, attackYOffsets[dir], 64, 64
                    );
                }
            }
        } catch (Exception e) {
            System.out.println("Error cargando sprites: " + e.getMessage());
        }
    }

    /**
     * Carga los fondos de escenarios.
     */
    private void loadBackgrounds() {
        try {
            BufferedImage bgSheet = ImageIO.read(getClass().getResource("/fondo/Escenarios.png"));
            int index = 0;
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 4; col++) {
                    backgrounds[index++] = bgSheet.getSubimage(
                        col * 128, row * 128, 128, 128
                    );
                }
            }
            int randomIndex = (int) (Math.random() * backgrounds.length);
            currentBackground = backgrounds[randomIndex];
        } catch (Exception e) {
            System.out.println("Error cargando fondos: " + e.getMessage());
        }
    }

    /**
     * Dibuja el juego: fondo, personajes, barras de vida.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int spriteBase = 64;
        double scale = Math.min(panelWidth, panelHeight) / 300.0;
        int spriteSize = (int) (spriteBase * scale);

        // Fondo
        if (currentBackground != null) {
            g.drawImage(currentBackground, 0, 0, panelWidth, panelHeight, null);
        }

        // --- Dibuja los PowerUps ---
        for (PowerUp powerUp : powerUps) {
            if (!powerUp.isCollected() && powerUp.getY() <= POWERUP_Y_LIMIT) {
                int px = (int) (powerUp.getX() * scale);
                int py = (int) (powerUp.getY() * scale);
                g.setColor(Color.MAGENTA);
                g.fillOval(px, py, spriteSize / 2, spriteSize / 2); // Orbe simple
                g.setColor(Color.BLACK);
                g.drawOval(px, py, spriteSize / 2, spriteSize / 2);
            }
        }

        // Boxeador
        int x = Math.max(0, Math.min((int) (boxer.getX() * scale), panelWidth - spriteSize));
        int y = Math.max(0, Math.min((int) (boxer.getY() * scale), panelHeight - spriteSize));
        BufferedImage boxerSprite;
        if (boxerAttackingJ) {
            boxerSprite = boxerAttackJ[boxerAttackFrame];
        } else if (boxerAttackingK) {
            boxerSprite = boxerAttackK[boxerAttackFrame];
        } else if (boxerWalkFrame > 0) {
            boxerSprite = "A".equals(ultimaDireccion) ? boxerWalk[1] : boxerWalk[0];
        } else {
            boxerSprite = boxerIdle[boxerIdleFrame % 2];
        }
        g.drawImage(boxerSprite, x, y, spriteSize, spriteSize, null);

        // Enemigo
        int enemigoIndex = 0;
        for (Enemy enemigoLoop : enemies) {
            int enemigoLoopXScaled = Math.max(0, Math.min((int) (enemigoLoop.getX() * scale), panelWidth - spriteSize));
            int enemigoLoopYScaled = Math.max(0, Math.min((int) (enemigoLoop.getY() * scale), panelHeight - spriteSize));
            // Usa los atributos del enemigo individual
            int dir = enemigoLoop.getDirection();
            int frame = enemigoLoop.getCurrentFrame();
            g.drawImage(walkAnimations[dir][frame], enemigoLoopXScaled, enemigoLoopYScaled, spriteSize, spriteSize, null);

            // Barra de vida del enemigo
            int vidaEnemigo = enemigoLoop.getHealth();
            int barraX = getWidth() - 220;
            int barraY = 50 + enemigoIndex * 30;
            g.setColor(Color.GREEN);
            g.fillRect(barraX, barraY, vidaEnemigo * 2, 20);
            g.setColor(Color.BLACK);
            g.drawRect(barraX, barraY, 200, 20);
            g.drawString("Enemigo " + (enemigoIndex + 1), barraX, barraY - 5);

            enemigoIndex++;
        }

        // Barra de vida del boxeador
        int vidaBoxer = boxer.getHealth();
        g.setColor(Color.RED);
        g.fillRect(20, 20, vidaBoxer * 2, 20);
        g.setColor(Color.BLACK);
        g.drawRect(20, 20, 200, 20);
        g.drawString("Jugador", 20, 15);

        // Dibuja hitbox del boxeador
        Rectangle boxerHitbox = boxer.getHitbox(spriteSize);
        g.setColor(new Color(255, 0, 0, 120)); // Rojo semi-transparente
        g.drawRect((int)(boxerHitbox.x * scale), (int)(boxerHitbox.y * scale), (int)(boxerHitbox.width * scale), (int)(boxerHitbox.height * scale));

        // Dibuja hitbox de los PowerUps
        for (PowerUp powerUp : powerUps) {
            if (!powerUp.isCollected() && powerUp.getY() <= POWERUP_Y_LIMIT) {
                Rectangle powerUpHitbox = powerUp.getHitbox(spriteSize / 2);
                g.setColor(new Color(0, 0, 255, 120)); // Azul semi-transparente
                g.drawRect((int)(powerUpHitbox.x * scale), (int)(powerUpHitbox.y * scale), (int)(powerUpHitbox.width * scale), (int)(powerUpHitbox.height * scale));
            }
        }

        // Dibuja hitbox de los enemigos
        for (Enemy enemigoLoop : enemies) {
            Rectangle enemyHitbox = new Rectangle(enemigoLoop.getX(), enemigoLoop.getY(), spriteSize, spriteSize);
            g.setColor(new Color(0, 255, 0, 120)); // Verde semi-transparente
            g.drawRect((int)(enemyHitbox.x * scale), (int)(enemyHitbox.y * scale), (int)(enemyHitbox.width * scale), (int)(enemyHitbox.height * scale));
        }

        for (Enemy enemy : enemies) {
            // Lógica de movimiento y ataque individual
            // Colisión con el boxeador
            Rectangle enemyHitbox = new Rectangle(enemy.getX(), enemy.getY(), spriteSize, spriteSize);
            if (boxerHitbox.intersects(enemyHitbox)) {
                // Daño al boxeador, etc.
            }
        }
    }
   
    // --- Métodos para animaciones y control ---

    public int getBoxerIdleFrame() { return boxerIdleFrame; }
    public void setBoxerIdleFrame(int frame) { this.boxerIdleFrame = frame; }

    public int getBoxerWalkFrame() { return boxerWalkFrame; }
    public void setBoxerWalkFrame(int frame) { this.boxerWalkFrame = frame; }

    public int getBoxerAttackFrame() { return boxerAttackFrame; }
    public void setBoxerAttackFrame(int frame) { this.boxerAttackFrame = frame; }

    public boolean isBoxerAttackingJ() { return boxerAttackingJ; }
    public void setBoxerAttackingJ(boolean attacking) { this.boxerAttackingJ = attacking; }

    public boolean isBoxerAttackingK() { return boxerAttackingK; }
    public void setBoxerAttackingK(boolean attacking) { this.boxerAttackingK = attacking; }

    public void setUltimaDireccion(String dir) { this.ultimaDireccion = dir; }

    public void setEnemigoAnimation(int direction, int frame) {
        this.enemigoDirection = direction;
        this.enemigoFrame = frame;
        this.enemigoAtacando = false;
        repaint();
    }

    public void setEnemigoAttackAnimation(int direction, int frame) {
        this.enemigoDirection = direction;
        this.enemigoFrame = frame;
        this.enemigoAtacando = true;
        repaint();
    }

    public void setEnemy(Enemy enemy) {
        if (enemy != null) {
            this.enemies.add(enemy);
        }
    }

    public void setEnemies(List<Enemy> enemies) {
        this.enemies = enemies;
    }

    public void actualizarVista() {
        repaint();
    }

    // --- Métodos para gestionar PowerUps ---
    public void addPowerUp(PowerUp powerUp) {
        powerUps.add(powerUp);
    }

    public List<PowerUp> getPowerUps() {
        return powerUps;
    }

    public void clearPowerUps() {
        powerUps.clear();
    }

    // --- UI y ciclo de vida ---

    public void initializeUI() {
        frame = new JFrame("Boxeo Educativo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(this);

        JButton btnSalir = new JButton("Salir");
        btnSalir.addActionListener(e -> {
            if (onSalir != null) onSalir.run();
        });
        JPanel panelBoton = new JPanel();
        panelBoton.add(btnSalir);
        frame.add(panelBoton, BorderLayout.SOUTH);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void addSalirListener(Runnable listener) {
        this.onSalir = listener;
    }

    public void closeGame() {
        if (frame != null) {
            frame.setVisible(false);
            frame.dispose();
        }
    }

    public int getPowerUpYLimit() {
        //Getter del power up y limit
        return POWERUP_Y_LIMIT;
    }

    public int mostrarPregunta(Question pregunta) {
        String[] opciones = pregunta.getOptions();
        int seleccion = JOptionPane.showOptionDialog(
            frame,
            pregunta.getQuestionText(),
            "Pregunta de Poder",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            opciones,
            opciones[0]
        );
        return seleccion; // Retorna el índice seleccionado (-1 si canceló)
    }
}