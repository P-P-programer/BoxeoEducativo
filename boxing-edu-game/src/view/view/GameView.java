package view;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import model.Boxer;

public class GameView extends JPanel {
    private JFrame frame;
    private BufferedImage[] boxerSprites;
    private Boxer boxer;

    public GameView(Boxer boxer) {
        this.boxer = boxer;
        loadSprites();
        setPreferredSize(new Dimension(400, 300));
        setBackground(Color.WHITE);
    }

    private void loadSprites() {
        boxerSprites = new BufferedImage[3];
        try {
            boxerSprites[0] = ImageIO.read(getClass().getResource("/recursos/boxeadores/Boxeador_0001.png"));
            boxerSprites[1] = ImageIO.read(getClass().getResource("/recursos/boxeadores/Boxeador_0002.png"));
            boxerSprites[2] = ImageIO.read(getClass().getResource("/recursos/boxeadores/Boxeador_0003.png"));
        } catch (Exception e) {
            System.out.println("Error cargando sprites: " + e.getMessage());
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (boxerSprites[0] != null && boxer != null) {
            g.drawImage(
                boxerSprites[boxer.getCurrentSprite()],
                boxer.getX(),
                boxer.getY(),
                null
            );
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

    public void actualizarVista() {
        repaint();
    }
}