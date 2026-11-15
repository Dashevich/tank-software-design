package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.math.Rectangle;

/**
 * Port. Drawable with a bounds rectangle.
 */
public interface BoundedDrawable extends Drawable {

    Rectangle getBounds();
}
