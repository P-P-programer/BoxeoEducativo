package model;

public class Boxer {
    private String name;
    private int health;
    private int strength;
    private int x, y;
    private int currentSprite;

    public Boxer(String name, int health, int strength, int x, int y) {
        this.name = name;
        this.health = health;
        this.strength = strength;
        this.x = x;
        this.y = y;
        this.currentSprite = 0;
    }

    public void move(int dx, int dy) {
        this.x += dx;
        this.y += dy;
        animate();
    }

    private void animate() {
        currentSprite = (currentSprite + 1) % 3; // Suponiendo 3 sprites
    }

    public void resetAnimation() {
        currentSprite = 0;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getCurrentSprite() { return currentSprite; }

    public void attack(Boxer opponent) {
        opponent.defend(strength);
    }

    public void defend(int damage) {
        health -= damage;
    }

    public boolean isKnockedOut() {
        return health <= 0;
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public int getStrength() {
        return strength;
    }
}