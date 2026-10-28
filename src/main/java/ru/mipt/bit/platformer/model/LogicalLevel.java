package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LogicalLevel {

    private Bounds bounds;
    private List<LevelEntity> entities = new ArrayList<LevelEntity>();
    private List<LevelListener> listeners = new ArrayList<LevelListener>();

    public LogicalLevel(Bounds bounds) {
        this.bounds = bounds;
    }

    public Bounds getBounds() {
        return bounds;
    }

    public void addListener(LevelListener listener) {
        listeners.add(listener);
    }

    public void removeListener(LevelListener listener) {
        listeners.remove(listener);
    }

    public void add(LevelEntity entity) {
        entities.add(entity);
        for (LevelListener listener : new ArrayList<LevelListener>(listeners)) {
            listener.onObjectAdded(entity);
        }
    }

    public void remove(LevelEntity entity) {
        if (!entities.remove(entity)) {
            return;
        }
        for (LevelListener listener : new ArrayList<LevelListener>(listeners)) {
            listener.onObjectRemoved(entity);
        }
    }

    public boolean contains(LevelEntity entity) {
        return entities.contains(entity);
    }

    public List<LevelEntity> getEntities() {
        return Collections.unmodifiableList(entities);
    }

    public List<Obstacle> getObstacles() {
        List<Obstacle> obstacles = new ArrayList<Obstacle>();
        for (LevelEntity entity : entities) {
            if (entity instanceof Obstacle) {
                obstacles.add((Obstacle) entity);
            }
        }
        return obstacles;
    }

    public List<LevelEntity> entitiesAt(GridPoint2 point) {
        List<LevelEntity> result = new ArrayList<LevelEntity>();
        for (LevelEntity entity : entities) {
            if (entity instanceof Obstacle && ((Obstacle) entity).occupies(point)) {
                result.add(entity);
            }
        }
        return result;
    }

    public void tick(float deltaTime) {
        List<LevelEntity> snapshot = new ArrayList<LevelEntity>(entities);
        for (LevelEntity entity : snapshot) {
            if (entities.contains(entity)) {
                entity.live(deltaTime);
            }
        }
    }
}
