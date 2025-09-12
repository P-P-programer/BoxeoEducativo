package view;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import model.Boxer;

public class GameView extends JPanel {
    private JFrame frame;
    private BufferedImage[] boxerSprites;
    private BufferedImage spriteSheet;
    private BufferedImage[][] walkAnimations; // [direction][frame]
    private int currentDirection = 0; // 0: arriba, 1: izquierda, 2: abajo, 3: derecha, 4: extra
    private int currentFrame = 0;
    private int frameCount = 9; // ajusta según tu sheet
    private Boxer boxer;

    private int enemigoX = 200, enemigoY = 200;
    private int enemigoDirection = 2; // 0: arriba, 1: izquierda, 2: abajo, 3: derecha
    private int enemigoFrame = 0;

    private Runnable onSalir;

    public GameView(Boxer boxer) {
        this.boxer = boxer;
        loadSprites();
        setPreferredSize(new Dimension(400, 300));
        setBackground(Color.WHITE);
    }

    private void loadSprites() {
        boxerSprites = new BufferedImage[3];
        try {
            boxerSprites[0] = ImageIO.read(getClass().getResource("/boxeadores/Boxeador_0001.png"));
            boxerSprites[1] = ImageIO.read(getClass().getResource("/boxeadores/Boxeador_0002.png"));
            boxerSprites[2] = ImageIO.read(getClass().getResource("/boxeadores/Boxeador_0003.png"));

            // Carga el sprite sheet LPC
            spriteSheet = ImageIO.read(getClass().getResource("/enemigos/enemigo1.png"));
            int frameWidth = 64;  // ajusta según tu sheet
            int frameHeight = 64; // ajusta según tu sheet

            walkAnimations = new BufferedImage[5][];
            for (int dir = 0; dir <= 4; dir++) {
                walkAnimations[dir] = new BufferedImage[frameCount];
                for (int i = 0; i < frameCount; i++) {
                    walkAnimations[dir][i] = spriteSheet.getSubimage(
                        (8 + dir) * frameWidth, // columna 8 a 12
                        i * frameHeight,        // fila i
                        frameWidth, frameHeight
                    );
                }
            }
        } catch (Exception e) {
            System.out.println("Error cargando sprites: " + e.getMessage());
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Dibuja el boxeador personalizado
        if (boxerSprites[0] != null && boxer != null) {
            g.drawImage(
                boxerSprites[boxer.getCurrentSprite()],
                boxer.getX(),
                boxer.getY(),
                null
            );
        }
        // Dibuja el enemigo LPC animado
        if (walkAnimations != null && walkAnimations[enemigoDirection] != null) {
            g.drawImage(
                walkAnimations[enemigoDirection][enemigoFrame],
                enemigoX,
                enemigoY,
                null
            );
        }
    }

    public void setAnimation(int direction, int frame) {
        this.currentDirection = direction;
        this.currentFrame = frame;
        repaint();
    }

    public void setEnemigoPosition(int x, int y) {
        this.enemigoX = x;
        this.enemigoY = y;
    }

    public void setEnemigoAnimation(int direction, int frame) {
        this.enemigoDirection = direction;
        this.enemigoFrame = frame;
        repaint();
    }

    public int getEnemigoX() { return enemigoX; }
    public int getEnemigoY() { return enemigoY; }

    public void initializeUI() {
        frame = new JFrame("Boxeo Educativo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(this);

        // --- Agrega el botón "Salir" ---
        JButton btnSalir = new JButton("Salir");
        btnSalir.addActionListener(e -> {
            if (onSalir != null) onSalir.run();
        });
        JPanel panelBoton = new JPanel();
        panelBoton.add(btnSalir);
        frame.add(panelBoton, BorderLayout.SOUTH);
        // --- Fin botón "Salir" ---

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void actualizarVista() {
        repaint();
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