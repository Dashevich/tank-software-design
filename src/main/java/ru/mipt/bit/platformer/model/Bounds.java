package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

public interface Bounds {

    boolean contains(GridPoint2 coordinates);
}
