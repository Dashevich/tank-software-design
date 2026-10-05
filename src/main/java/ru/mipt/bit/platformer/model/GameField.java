package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

public class GameField implements Bounds {

    public static final int WIDTH = 10;
    public static final int HEIGHT = 8;

    private int width;
    private int height;

    public GameField(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public boolean contains(GridPoint2 coordinates) {
        return coordinates.x >= 0
                && coordinates.y >= 0
                && coordinates.x < width
                && coordinates.y < height;
    }
}
