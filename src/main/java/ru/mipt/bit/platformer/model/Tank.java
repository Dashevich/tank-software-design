package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import java.util.List;

public class Tank implements LevelEntity, Movable, Controllable, Obstacle, Shootable {

    private static final float FINISHED_MOVEMENT_PROGRESS = 1f;
    private static final float FLOAT_EQUALITY_THRESHOLD = 0.000001f;

    private GridPoint2 coordinates;
    private GridPoint2 destinationCoordinates;
    private float movementProgress = FINISHED_MOVEMENT_PROGRESS;
    private Direction orientation;
    private float movementSpeed;
    private int health;
    private int bulletDamage;
    private float bulletSpeed;

    public Tank(
            GridPoint2 startCoordinates,
            float movementSpeed,
            int health,
            int bulletDamage,
            float bulletSpeed
    ) {
        destinationCoordinates = new GridPoint2(startCoordinates);
        coordinates = new GridPoint2(destinationCoordinates);
        orientation = Direction.RIGHT;
        this.movementSpeed = movementSpeed;
        this.health = health;
        this.bulletDamage = bulletDamage;
        this.bulletSpeed = bulletSpeed;
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
        return orientation.getRotation();
    }

    public Direction getOrientation() {
        return orientation;
    }

    public int getHealth() {
        return health;
    }

    public boolean isDestroyed() {
        return health <= 0;
    }

    public void takeDamage(int damage) {
        health -= damage;
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
        orientation = direction;
        GridPoint2 nextCoordinates = direction.nextCoordinates(coordinates);
        if (bounds.contains(nextCoordinates) && !isOccupiedByOthers(nextCoordinates, obstacles)) {
            destinationCoordinates.set(nextCoordinates);
            movementProgress = 0f;
        }
    }

    @Override
    public void shoot(LogicalLevel level) {
        GridPoint2 spawnPoint = orientation.nextCoordinates(coordinates);
        if (!level.getBounds().contains(spawnPoint)) {
            return;
        }
        Bullet bullet = new Bullet(level, this, spawnPoint, orientation, bulletDamage, bulletSpeed);
        level.add(bullet);
    }

    @Override
    public void live(float deltaTime) {
        movementProgress = continueProgress(movementProgress, deltaTime, movementSpeed);
        if (hasFinishedMovement()) {
            coordinates.set(destinationCoordinates);
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
