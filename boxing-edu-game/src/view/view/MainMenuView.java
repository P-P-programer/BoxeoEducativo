package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public class MainMenuView extends JFrame {
    private BufferedImage backgroundImage;
    private Runnable onStartGame;
    private Runnable onExit;

    // Coordenadas de los botones en la imagen (ajusta según tu PNG)
    private final Rectangle btnNuevoJuego = new Rectangle(440, 220, 400, 60);
    private final Rectangle btnSalir = new Rectangle(440, 460, 400, 60);

    public MainMenuView() {
        setTitle("Boxeo Educativo - Menú Principal");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Carga la imagen usando BufferedImage y getResource
        try {
            System.out.println(getClass().getResource("/UI/Menu.png"));
            backgroundImage = ImageIO.read(getClass().getResource("/UI/Menu.png"));
        } catch (IOException | IllegalArgumentException e) {
            System.out.println("No se pudo cargar el fondo: " + e.getMessage());
        }

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        setContentPane(panel);

        // Detecta clics en el panel
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point p = e.getPoint();
                if (btnNuevoJuego.contains(p) && onStartGame != null) {
                    onStartGame.run();
                } else if (btnSalir.contains(p) && onExit != null) {
                    onExit.run();
                }
            }
        });
    }

    public void setStartGameListener(Runnable listener) {
        this.onStartGame = listener;
    }

    public void setExitListener(Runnable listener) {
        this.onExit = listener;
    }

    public void initializeMenu() {
        setVisible(true);
    }

    public void closeMenu() {
        setVisible(false);
        dispose();
    }
}