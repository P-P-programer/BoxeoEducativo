package view;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import model.Boxer;
import model.Enemy;

/**
 * Vista principal del juego.
 * Dibuja personajes, fondos, barras de vida y gestiona la UI.
 * Futuras mejoras: efectos visuales, animaciones de K.O., bloqueo, etc.
 */
public class GameView extends JPanel {
    // --- Referencias a los modelos ---
    private Boxer boxer;
    private Enemy enemy;

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

    // --- Constructor ---
    public GameView(Boxer boxer, Enemy enemy) {
        this.boxer = boxer;
        this.enemy = enemy;
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
        int enemigoXScaled = Math.max(0, Math.min((int) (enemy.getX() * scale), panelWidth - spriteSize));
        int enemigoYScaled = Math.max(0, Math.min((int) (enemy.getY() * scale), panelHeight - spriteSize));
        if (enemigoAtacando) {
            int frame = Math.max(0, Math.min(enemigoFrame, 5));
            g.drawImage(attackAnimations[enemigoDirection][frame], enemigoXScaled, enemigoYScaled, spriteSize, spriteSize, null);
        } else {
            g.drawImage(walkAnimations[enemigoDirection][enemigoFrame], enemigoXScaled, enemigoYScaled, spriteSize, spriteSize, null);
        }

        // Barra de vida del boxeador
        int vidaBoxer = boxer.getHealth();
        g.setColor(Color.RED);
        g.fillRect(20, 20, vidaBoxer * 2, 20);
        g.setColor(Color.BLACK);
        g.drawRect(20, 20, 200, 20);
        g.drawString("Jugador", 20, 15);

        // Barra de vida del enemigo
        int vidaEnemigo = enemy.getHealth();
        g.setColor(Color.GREEN);
        g.fillRect(getWidth() - 220, 20, vidaEnemigo * 2, 20);
        g.setColor(Color.BLACK);
        g.drawRect(getWidth() - 220, 20, 200, 20);
        g.drawString("Enemigo", getWidth() - 220, 15);
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
        this.enemy = enemy;
    }

    public void actualizarVista() {
        repaint();
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
}