package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

public class Tree implements LevelEntity, Obstacle, Positioned {

    private GridPoint2 coordinates;

    public Tree(GridPoint2 coordinates) {
        this.coordinates = new GridPoint2(coordinates);
    }

    @Override
    public GridPoint2 getCoordinates() {
        return coordinates;
    }

    @Override
    public boolean occupies(GridPoint2 point) {
        return coordinates.equals(point);
    }

    @Override
    public void live(float deltaTime) {
    }
}
