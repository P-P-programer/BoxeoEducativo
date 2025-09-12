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
    public BufferedImage[] getAnimation(int col, int frameCount) {
        BufferedImage[] anim = new BufferedImage[frameCount];
        for (int i = 0; i < frameCount; i++) {
            anim[i] = sheet.getSubimage(
                col * frameWidth,   // x
                i * frameHeight,    // y
                frameWidth, frameHeight
            );
        }
        return anim;
    }
}