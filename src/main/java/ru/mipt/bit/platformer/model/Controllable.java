package ru.mipt.bit.platformer.model;

import java.util.List;

/**
 * Domain. Can be told to move.
 */
public interface Controllable {

    void tryMove(Direction direction, Bounds bounds, List<Obstacle> obstacles);
}
