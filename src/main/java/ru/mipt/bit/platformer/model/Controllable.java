package ru.mipt.bit.platformer.model;

import java.util.List;

public interface Controllable {

    void tryMove(Direction direction, Bounds bounds, List<Obstacle> obstacles);
}
