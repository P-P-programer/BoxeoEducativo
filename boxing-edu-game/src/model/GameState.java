public class GameState {
    private int currentRound;
    private int playerScore;
    private int enemyScore;

    public GameState() {
        this.currentRound = 1;
        this.playerScore = 0;
        this.enemyScore = 0;
    }

    public void resetGame() {
        this.currentRound = 1;
        this.playerScore = 0;
        this.enemyScore = 0;
    }

    public void updateScores(int playerPoints, int enemyPoints) {
        this.playerScore += playerPoints;
        this.enemyScore += enemyPoints;
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public void nextRound() {
        this.currentRound++;
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public int getEnemyScore() {
        return enemyScore;
    }
}