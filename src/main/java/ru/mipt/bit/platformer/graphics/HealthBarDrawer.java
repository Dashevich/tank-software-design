package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.model.HasHealth;

/**
 * Port. Draw an HP bar.
 * Also knows LibGDX Batch.
 */
public interface HealthBarDrawer {

    void draw(Batch batch, Rectangle bounds, HasHealth hasHealth);

    void dispose();
}
