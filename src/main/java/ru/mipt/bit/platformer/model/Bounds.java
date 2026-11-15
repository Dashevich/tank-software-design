package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

/**
 * Domain. Checks if a cell is on the field.
 */
public interface Bounds {

    boolean contains(GridPoint2 coordinates);
}
