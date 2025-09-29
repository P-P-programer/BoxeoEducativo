package model;

import java.awt.Rectangle;

public class Boxer {
    private String name;
    private int health;
    private int strength;
    private int x, y;
    private int currentSprite;
    private double yPosVirtual = 0; // Altura sobre el suelo (en "pixeles virtuales")
    private double yVel = 0;
    private boolean enSuelo = true;

    public Boxer(String name, int health, int strength, int x, int y) {
        this.name = name;
        this.health = health;
        this.strength = strength;
        this.x = x;
        this.y = y;
        this.currentSprite = 0;
    }

    public void move(int dx, int dy, int minX, int maxX, int minY, int maxY) {
        this.x = Math.max(minX, Math.min(this.x + dx, maxX));
        this.y = Math.max(minY, Math.min(this.y + dy, maxY));
        animate();
    }

    private void animate() {
        currentSprite = (currentSprite + 1) % 3; // Suponiendo 3 sprites
    }

    public void resetAnimation() {
        currentSprite = 0;
    }

    public int getX() { return x; }
    public void setX(int x) { this.x = x; }
    public int getY() { return y; }
    public void setY(int y) { this.y = y; }
    public int getCurrentSprite() { return currentSprite; }

    public void attack(Boxer opponent) {
        opponent.defend(strength);
    }

    public void defend(int damage) {
        health -= damage;
    }

    public void recibirDanio(int cantidad) {
        this.health = Math.max(0, this.health - cantidad);
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

    public double getYPosVirtual() { return yPosVirtual; }
    public void setYPosVirtual(double y) { this.yPosVirtual = y; }
    public double getYVel() { return yVel; }
    public void setYVel(double v) { this.yVel = v; }
    public boolean isEnSuelo() { return enSuelo; }
    public void setEnSuelo(boolean suelo) { this.enSuelo = suelo; }

    public Rectangle getHitbox(int spriteSize) {
        int hitboxSize = spriteSize / 2;
        int hitboxX = x + (spriteSize - hitboxSize) / 2;
        int hitboxY = y + (spriteSize - hitboxSize) / 2;
        return new Rectangle(hitboxX, hitboxY, hitboxSize, hitboxSize);
    }

    public void curar(int cantidad) {
        this.health = Math.min(100, this.health + cantidad);
    }
}