package view;

import java.awt.image.BufferedImage;

//clase para sacar sprites enemigos

public class SpriteSheet {
    private BufferedImage sheet;
    private int frameWidth, frameHeight;

    public SpriteSheet(BufferedImage sheet, int frameWidth, int frameHeight) {
        this.sheet = sheet;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
    }

    // Extrae una animación de una columna (frames en vertical)
    public BufferedImage[] getAnimation(int col, int frameCount, int yOffset) {
        BufferedImage[] anim = new BufferedImage[frameCount];
        for (int i = 0; i < frameCount; i++) {
            int x = col * frameWidth;
            int y = yOffset + i * frameHeight;
            System.out.println("Extrayendo frame: col=" + col + " fila=" + i + " x=" + x + " y=" + y);
            anim[i] = sheet.getSubimage(x, y, frameWidth, frameHeight);
        }
        return anim;
    }
}