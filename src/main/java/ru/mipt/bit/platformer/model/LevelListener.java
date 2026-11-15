package ru.mipt.bit.platformer.model;

/**
 * Port. Notify when an object is added or removed.
 */
public interface LevelListener {

    void onObjectAdded(LevelEntity entity);

    void onObjectRemoved(LevelEntity entity);
}
