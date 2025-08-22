package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class GameView extends JPanel {
    private JFrame frame;
    private BufferedImage[] boxerSprites;
    private int currentSprite = 0;
    private int x = 150, y = 100;
    private boolean moving = false;

    public GameView() {
        loadSprites();
        setPreferredSize(new Dimension(400, 300));
        setBackground(Color.WHITE);

        // Key bindings para WASD
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("W"), "moveUp");
        getActionMap().put("moveUp", new MoveAction(0, -10));
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("S"), "moveDown");
        getActionMap().put("moveDown", new MoveAction(0, 10));
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("A"), "moveLeft");
        getActionMap().put("moveLeft", new MoveAction(-10, 0));
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("D"), "moveRight");
        getActionMap().put("moveRight", new MoveAction(10, 0));
    }

    private void loadSprites() {
        boxerSprites = new BufferedImage[3];
        try {
            boxerSprites[0] = ImageIO.read(new File("recursos/boxeadores/Boxeador_0001.png"));
            boxerSprites[1] = ImageIO.read(new File("recursos/boxeadores/Boxeador_0002.png"));
            boxerSprites[2] = ImageIO.read(new File("recursos/boxeadores/Boxeador_0003.png"));
        } catch (Exception e) {
            System.out.println("Error cargando sprites: " + e.getMessage());
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (boxerSprites[0] != null) {
            g.drawImage(boxerSprites[currentSprite], x, y, null);
        }
    }

    public void initializeUI() {
        frame = new JFrame("Boxeo Educativo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(this);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private class MoveAction extends AbstractAction {
        private int dx, dy;
        public MoveAction(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            x += dx;
            y += dy;
            moving = (dx != 0 || dy != 0);
            if (moving) {
                currentSprite = (currentSprite + 1) % 3; // Cambia sprite solo si se mueve
            } else {
                currentSprite = 0; // Quieto
            }
            repaint();
        }
    }
}