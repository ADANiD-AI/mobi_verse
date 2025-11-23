package com.zai.mobiverse;

public class Game {
    private int score;
    private int level;

    public Game() {
        this.score = 0;
        this.level = 1;
    }

    public void increaseScore(int points) {
        this.score += points;
    }

    public void levelUp() {
        this.level++;
    }

    public int getScore() {
        return score;
    }

    public int getLevel() {
        return level;
    }
}
