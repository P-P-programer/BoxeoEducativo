public class Boxer {
    private String name;
    private int health;
    private int strength;

    public Boxer(String name, int health, int strength) {
        this.name = name;
        this.health = health;
        this.strength = strength;
    }

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