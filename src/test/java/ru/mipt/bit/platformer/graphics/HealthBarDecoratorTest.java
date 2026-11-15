package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import org.junit.Test;
import ru.mipt.bit.platformer.model.HasHealth;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class HealthBarDecoratorTest {

    @Test
    public void alwaysDrawsDelegate() {
        RecordingDrawable delegate = new RecordingDrawable();
        RecordingHealthBarDrawer drawer = new RecordingHealthBarDrawer();
        HealthBarsToggle toggle = new HealthBarsToggle();
        HealthBarDecorator decorator = new HealthBarDecorator(
                delegate,
                new FixedBounds(new Rectangle(0, 0, 10, 10)),
                new FixedHealth(80, 100),
                toggle,
                drawer
        );

        decorator.draw(null);

        assertEquals(1, delegate.drawCalls);
        assertEquals(0, drawer.drawCalls);
    }

    @Test
    public void drawsHealthBarOnlyWhenToggleIsOn() {
        RecordingDrawable delegate = new RecordingDrawable();
        RecordingHealthBarDrawer drawer = new RecordingHealthBarDrawer();
        HealthBarsToggle toggle = new HealthBarsToggle();
        HealthBarDecorator decorator = new HealthBarDecorator(
                delegate,
                new FixedBounds(new Rectangle(1, 2, 3, 4)),
                new FixedHealth(50, 100),
                toggle,
                drawer
        );

        decorator.draw(null);
        assertEquals(0, drawer.drawCalls);

        toggle.toggle();
        decorator.draw(null);

        assertEquals(2, delegate.drawCalls);
        assertEquals(1, drawer.drawCalls);
        assertTrue(drawer.lastHealth.getHealth() == 50);
    }

    @Test
    public void disposeDisposesDelegateAndDrawer() {
        RecordingDrawable delegate = new RecordingDrawable();
        RecordingHealthBarDrawer drawer = new RecordingHealthBarDrawer();
        HealthBarDecorator decorator = new HealthBarDecorator(
                delegate,
                new FixedBounds(new Rectangle()),
                new FixedHealth(1, 1),
                new HealthBarsToggle(),
                drawer
        );

        decorator.dispose();

        assertTrue(delegate.disposed);
        assertTrue(drawer.disposed);
        assertFalse(drawer.drawCalls > 0 && !delegate.disposed);
    }

    private static class RecordingDrawable implements Drawable {
        private int drawCalls;
        private boolean disposed;

        @Override
        public void draw(Batch batch) {
            drawCalls++;
        }

        @Override
        public void dispose() {
            disposed = true;
        }
    }

    private static class FixedBounds implements BoundedDrawable {
        private Rectangle bounds;

        private FixedBounds(Rectangle bounds) {
            this.bounds = bounds;
        }

        @Override
        public Rectangle getBounds() {
            return bounds;
        }

        @Override
        public void draw(Batch batch) {
        }

        @Override
        public void dispose() {
        }
    }

    private static class FixedHealth implements HasHealth {
        private int health;
        private int maxHealth;

        private FixedHealth(int health, int maxHealth) {
            this.health = health;
            this.maxHealth = maxHealth;
        }

        @Override
        public int getHealth() {
            return health;
        }

        @Override
        public int getMaxHealth() {
            return maxHealth;
        }
    }

    private static class RecordingHealthBarDrawer implements HealthBarDrawer {
        private int drawCalls;
        private HasHealth lastHealth;
        private boolean disposed;

        @Override
        public void draw(Batch batch, Rectangle bounds, HasHealth hasHealth) {
            drawCalls++;
            lastHealth = hasHealth;
        }

        @Override
        public void dispose() {
            disposed = true;
        }
    }
}
