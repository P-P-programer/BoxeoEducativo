package model;

import java.awt.Rectangle;

public class PowerUp {
    private int x;
    private int y;
    private boolean collected;

    public PowerUp(int x, int y) {
        this.x = x;
        this.y = y;
        this.collected = false;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public boolean isCollected() { return collected; }
    public void setCollected(boolean collected) { this.collected = collected; }

    public Rectangle getHitbox(int size) {
        return new Rectangle(x, y, size, size);
    }
}