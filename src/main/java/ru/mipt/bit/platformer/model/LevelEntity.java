package ru.mipt.bit.platformer.model;

/**
 * Domain. Lives on the level and ticks.
 */
public interface LevelEntity {

    void live(float deltaTime);
}
