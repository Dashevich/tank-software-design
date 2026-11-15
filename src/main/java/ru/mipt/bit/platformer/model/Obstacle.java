package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

/**
 * Domain. Occupies cells.
 */
public interface Obstacle {

    boolean occupies(GridPoint2 point);
}
