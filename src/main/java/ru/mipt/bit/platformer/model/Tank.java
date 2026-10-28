package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import java.util.List;

public class Tank implements Movable, Controllable, Obstacle {

    private static final float FINISHED_MOVEMENT_PROGRESS = 1f;
    private static final float FLOAT_EQUALITY_THRESHOLD = 0.000001f;

    private GridPoint2 coordinates;
    private GridPoint2 destinationCoordinates;
    private float movementProgress = FINISHED_MOVEMENT_PROGRESS;
    private float rotation;
    private float movementSpeed;

    public Tank(GridPoint2 startCoordinates, float movementSpeed) {
        destinationCoordinates = new GridPoint2(startCoordinates);
        coordinates = new GridPoint2(destinationCoordinates);
        rotation = Direction.RIGHT.getRotation();
        this.movementSpeed = movementSpeed;
    }

    @Override
    public GridPoint2 getCoordinates() {
        return coordinates;
    }

    @Override
    public GridPoint2 getDestinationCoordinates() {
        return destinationCoordinates;
    }

    @Override
    public float getMovementProgress() {
        return movementProgress;
    }

    @Override
    public float getRotation() {
        return rotation;
    }

    public boolean hasFinishedMovement() {
        return isEqual(movementProgress, FINISHED_MOVEMENT_PROGRESS);
    }

    @Override
    public boolean occupies(GridPoint2 point) {
        if (coordinates.equals(point)) {
            return true;
        }
        return !hasFinishedMovement() && destinationCoordinates.equals(point);
    }

    @Override
    public void tryMove(Direction direction, Bounds bounds, List<Obstacle> obstacles) {
        if (!hasFinishedMovement()) {
            return;
        }
        rotation = direction.getRotation();
        GridPoint2 nextCoordinates = direction.nextCoordinates(coordinates);
        if (bounds.contains(nextCoordinates) && !isOccupiedByOthers(nextCoordinates, obstacles)) {
            destinationCoordinates.set(nextCoordinates);
            movementProgress = 0f;
        }
    }

    private boolean isOccupiedByOthers(GridPoint2 coordinates, List<Obstacle> obstacles) {
        for (Obstacle obstacle : obstacles) {
            if (obstacle == this) {
                continue;
            }
            if (obstacle.occupies(coordinates)) {
                return true;
            }
        }
        return false;
    }

    public void update(float deltaTime) {
        movementProgress = continueProgress(movementProgress, deltaTime, movementSpeed);
        if (hasFinishedMovement()) {
            coordinates.set(destinationCoordinates);
        }
    }

    private static float continueProgress(float previousProgress, float deltaTime, float speed) {
        float nextProgress = previousProgress + deltaTime / speed;
        if (nextProgress < 0f) {
            return 0f;
        }
        if (nextProgress > FINISHED_MOVEMENT_PROGRESS) {
            return FINISHED_MOVEMENT_PROGRESS;
        }
        return nextProgress;
    }

    private static boolean isEqual(float left, float right) {
        return Math.abs(left - right) < FLOAT_EQUALITY_THRESHOLD;
    }
}
