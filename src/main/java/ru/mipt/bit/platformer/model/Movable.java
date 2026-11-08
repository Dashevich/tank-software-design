package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

public interface Movable extends Positioned {

    GridPoint2 getDestinationCoordinates();

    float getMovementProgress();

    float getRotation();
}
