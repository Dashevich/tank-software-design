package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.math.Rectangle;

public interface BoundedDrawable extends Drawable {

    Rectangle getBounds();
}
