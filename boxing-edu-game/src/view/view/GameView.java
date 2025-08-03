package view;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class GameView {
    private JFrame frame;

    public void initializeUI() {
        frame = new JFrame("Boxeo Educativo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null); // Centra la ventana
        frame.add(new JLabel("¡Bienvenido al juego de boxeo educativo!", JLabel.CENTER));
        frame.setVisible(true);
    }

    public void updateDisplay() {
        // Aquí iría el código para actualizar la interfaz según el estado del juego
    }

    public void showGameOverScreen() {
        // Aquí podrías mostrar una pantalla de "Game Over"
    }
}