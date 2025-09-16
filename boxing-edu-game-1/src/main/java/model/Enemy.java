package model;

public class Enemy {
    private int health;
    private int strength;
    private int x, y;
    private int direction;
    private int currentFrame;

    public Enemy(int health, int strength, int x, int y) {
        this.health = health;
        this.strength = strength;
        this.x = x;
        this.y = y;
        this.direction = 2; // por ejemplo, abajo
        this.currentFrame = 0;
    }

    public void move(int dx, int dy) {
        this.x += dx;
        this.y += dy;
    }

    public void recibirDanio(int cantidad) {
        this.health = Math.max(0, this.health - cantidad);
    }

    public boolean isKnockedOut() {
        return health <= 0;
    }

    public int getHealth() {
        return health;
    }
    public int getStrength() { return strength; }
    public int getX() { return x; }
    public void setX(int x) { this.x = x; }
    public int getY() { return y; }
    public void setY(int y) { this.y = y; }
    public int getDirection() { return direction; }
    public void setDirection(int dir) { this.direction = dir; }
    public int getCurrentFrame() { return currentFrame; }
    public void setCurrentFrame(int frame) { this.currentFrame = frame; }
}
