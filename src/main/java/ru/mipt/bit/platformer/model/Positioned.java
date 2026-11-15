package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

/**
 * Domain. Has coordinates.
 */
public interface Positioned {

    GridPoint2 getCoordinates();
}
