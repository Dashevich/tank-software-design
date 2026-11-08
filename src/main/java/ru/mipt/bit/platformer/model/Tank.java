package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import java.util.List;

public class Tank {

    private static final float MOVEMENT_SPEED = 0.4f;
    private static final float FINISHED_MOVEMENT_PROGRESS = 1f;
    private static final float FLOAT_EQUALITY_THRESHOLD = 0.000001f;

    private GridPoint2 coordinates;
    private GridPoint2 destinationCoordinates;
    private float movementProgress = FINISHED_MOVEMENT_PROGRESS;
    private float rotation;

    public Tank(GridPoint2 startCoordinates) {
        destinationCoordinates = new GridPoint2(startCoordinates);
        coordinates = new GridPoint2(destinationCoordinates);
        rotation = Direction.RIGHT.getRotation();
    }

    public GridPoint2 getCoordinates() {
        return coordinates;
    }

    public GridPoint2 getDestinationCoordinates() {
        return destinationCoordinates;
    }

    public float getMovementProgress() {
        return movementProgress;
    }

    public float getRotation() {
        return rotation;
    }

    public boolean hasFinishedMovement() {
        return isEqual(movementProgress, FINISHED_MOVEMENT_PROGRESS);
    }

    public void tryMove(Direction direction, GameField field, List<Tree> trees) {
        if (!hasFinishedMovement()) {
            return;
        }
        rotation = direction.getRotation();
        GridPoint2 nextCoordinates = direction.nextCoordinates(coordinates);
        if (field.contains(nextCoordinates) && !isOccupied(nextCoordinates, trees)) {
            destinationCoordinates.set(nextCoordinates);
            movementProgress = 0f;
        }
    }

    private static boolean isOccupied(GridPoint2 coordinates, List<Tree> trees) {
        for (Tree tree : trees) {
            if (tree.occupies(coordinates)) {
                return true;
            }
        }
        return false;
    }

    public void update(float deltaTime) {
        movementProgress = continueProgress(movementProgress, deltaTime, MOVEMENT_SPEED);
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
