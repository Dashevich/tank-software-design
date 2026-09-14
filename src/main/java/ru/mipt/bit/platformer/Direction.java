package ru.mipt.bit.platformer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.GridPoint2;

public enum Direction {
    UP(0, 1, 90f, Input.Keys.UP, Input.Keys.W),
    LEFT(-1, 0, -180f, Input.Keys.LEFT, Input.Keys.A),
    DOWN(0, -1, -90f, Input.Keys.DOWN, Input.Keys.S),
    RIGHT(1, 0, 0f, Input.Keys.RIGHT, Input.Keys.D);

    private int deltaX;
    private int deltaY;
    private float rotation;
    private int[] keys;

    Direction(int deltaX, int deltaY, float rotation, int... keys) {
        this.deltaX = deltaX;
        this.deltaY = deltaY;
        this.rotation = rotation;
        this.keys = keys;
    }

    public float getRotation() {
        return rotation;
    }

    public GridPoint2 nextCoordinates(GridPoint2 current) {
        return new GridPoint2(current).add(deltaX, deltaY);
    }

    public boolean isPressed() {
        for (int key : keys) {
            if (Gdx.input.isKeyPressed(key)) {
                return true;
            }
        }
        return false;
    }
}
