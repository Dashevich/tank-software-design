package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * Port. Draw an object.
 * Now uses LibGDX Batch in the method - that leaks infrastructure.
 */
public interface Drawable {

    void draw(Batch batch);

    void dispose();
}
