package ru.mipt.bit.platformer.model;

public interface LevelListener {

    void onObjectAdded(LevelEntity entity);

    void onObjectRemoved(LevelEntity entity);
}
