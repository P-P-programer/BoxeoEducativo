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

    // Tamaño base de la imagen
    private static final int BASE_WIDTH = 1280;
    private static final int BASE_HEIGHT = 720;

    // Coordenadas y tamaños originales (en base 1280x720)
    private static final Rectangle BTN_NUEVO_JUEGO_BASE = new Rectangle(395, 285, 330, 70); //coordenadas y tamaño del botón "Nuevo Juego"
    private static final Rectangle BTN_SALIR_BASE = new Rectangle(395, 570, 330, 70); //coordenadas y tamaño del botón "Salir"

    // Constructor
    public MainMenuView() {
        setTitle("Boxeo Educativo - Menú Principal");
        setSize(BASE_WIDTH, BASE_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            backgroundImage = ImageIO.read(getClass().getResource("/UI/Menu.png"));
        } catch (IOException | IllegalArgumentException e) {
            System.out.println("No se pudo cargar el fondo: " + e.getMessage());
        }

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int w = getWidth();
                int h = getHeight();
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, w, h, this);
                }
                // Calcula los rectángulos escalados
                Rectangle btnNuevoJuego = scaleRect(BTN_NUEVO_JUEGO_BASE, w, h);
                Rectangle btnSalir = scaleRect(BTN_SALIR_BASE, w, h);

                // Dibuja los rectángulos para depuración
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(new Color(255, 0, 0, 100));
                g2d.fill(btnNuevoJuego);
                g2d.setColor(Color.RED);
                g2d.draw(btnNuevoJuego);

                g2d.setColor(new Color(0, 0, 255, 100));
                g2d.fill(btnSalir);
                g2d.setColor(Color.BLUE);
                g2d.draw(btnSalir);
            }
        };
        setContentPane(panel);

        // Detecta clics en el panel
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int w = panel.getWidth();
                int h = panel.getHeight();
                Rectangle btnNuevoJuego = scaleRect(BTN_NUEVO_JUEGO_BASE, w, h);
                Rectangle btnSalir = scaleRect(BTN_SALIR_BASE, w, h);

                Point p = e.getPoint();
                if (btnNuevoJuego.contains(p) && onStartGame != null) {
                    onStartGame.run();
                } else if (btnSalir.contains(p) && onExit != null) {
                    onExit.run();
                }
            }
        });
    }

    // Escala un rectángulo base al tamaño actual del panel
    private Rectangle scaleRect(Rectangle base, int w, int h) {
        int x = (int) (base.x * w / (double) BASE_WIDTH);
        int y = (int) (base.y * h / (double) BASE_HEIGHT);
        int width = (int) (base.width * w / (double) BASE_WIDTH);
        int height = (int) (base.height * h / (double) BASE_HEIGHT);
        return new Rectangle(x, y, width, height);
    }

    // funciones responsivas
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