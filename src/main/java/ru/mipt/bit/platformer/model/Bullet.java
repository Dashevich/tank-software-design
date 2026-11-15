package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

public class Bullet implements LevelEntity, Obstacle, Movable {

    private static final float FINISHED_MOVEMENT_PROGRESS = 1f;
    private static final float FLOAT_EQUALITY_THRESHOLD = 0.000001f;

    private LogicalLevel level;
    private Tank owner;
    private GridPoint2 coordinates;
    private GridPoint2 destinationCoordinates;
    private Direction direction;
    private int damage;
    private float movementSpeed;
    private float movementProgress = FINISHED_MOVEMENT_PROGRESS;

    public Bullet(
            LogicalLevel level,
            Tank owner,
            GridPoint2 startCoordinates,
            Direction direction,
            int damage,
            float movementSpeed
    ) {
        this.level = level;
        this.owner = owner;
        this.coordinates = new GridPoint2(startCoordinates);
        this.destinationCoordinates = new GridPoint2(startCoordinates);
        this.direction = direction;
        this.damage = damage;
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
        return direction.getRotation();
    }

    public Direction getDirection() {
        return direction;
    }

    public int getDamage() {
        return damage;
    }

    public Tank getOwner() {
        return owner;
    }

    @Override
    public boolean occupies(GridPoint2 point) {
        if (coordinates.equals(point)) {
            return true;
        }
        return !hasFinishedMovement() && destinationCoordinates.equals(point);
    }

    @Override
    public void live(float deltaTime) {
        if (!level.contains(this)) {
            return;
        }
        if (resolveCollisionsAt(coordinates)) {
            return;
        }
        if (hasFinishedMovement()) {
            startFlightToNextCell();
            return;
        }
        movementProgress = continueProgress(movementProgress, deltaTime, movementSpeed);
        if (hasFinishedMovement()) {
            coordinates.set(destinationCoordinates);
            resolveCollisionsAt(coordinates);
        }
    }

    private void startFlightToNextCell() {
        GridPoint2 next = direction.nextCoordinates(coordinates);
        if (!level.getBounds().contains(next)) {
            level.remove(this);
            return;
        }
        destinationCoordinates.set(next);
        movementProgress = 0f;
    }

    private boolean resolveCollisionsAt(GridPoint2 point) {
        for (LevelEntity entity : level.entitiesAt(point)) {
            if (entity == this) {
                continue;
            }
            if (entity == owner) {
                continue;
            }
            if (entity instanceof Tank) {
                Tank tank = (Tank) entity;
                tank.takeDamage(damage);
                if (tank.isDestroyed()) {
                    level.remove(tank);
                }
                level.remove(this);
                return true;
            }
            if (entity instanceof Tree || entity instanceof Bullet) {
                level.remove(this);
                if (entity instanceof Bullet) {
                    level.remove(entity);
                }
                return true;
            }
        }
        return false;
    }

    private boolean hasFinishedMovement() {
        return isEqual(movementProgress, FINISHED_MOVEMENT_PROGRESS);
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
