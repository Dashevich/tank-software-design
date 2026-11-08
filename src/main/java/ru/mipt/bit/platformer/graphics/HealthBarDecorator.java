package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import ru.mipt.bit.platformer.model.HasHealth;

public class HealthBarDecorator implements Drawable {

    private Drawable delegate;
    private BoundedDrawable boundsSource;
    private HasHealth hasHealth;
    private HealthBarsToggle toggle;
    private HealthBarDrawer healthBarDrawer;

    public HealthBarDecorator(
            Drawable delegate,
            BoundedDrawable boundsSource,
            HasHealth hasHealth,
            HealthBarsToggle toggle
    ) {
        this(delegate, boundsSource, hasHealth, toggle, new TextureHealthBarDrawer());
    }

    public HealthBarDecorator(
            Drawable delegate,
            BoundedDrawable boundsSource,
            HasHealth hasHealth,
            HealthBarsToggle toggle,
            HealthBarDrawer healthBarDrawer
    ) {
        this.delegate = delegate;
        this.boundsSource = boundsSource;
        this.hasHealth = hasHealth;
        this.toggle = toggle;
        this.healthBarDrawer = healthBarDrawer;
    }

    @Override
    public void draw(Batch batch) {
        delegate.draw(batch);
        if (toggle.isVisible()) {
            healthBarDrawer.draw(batch, boundsSource.getBounds(), hasHealth);
        }
    }

    @Override
    public void dispose() {
        delegate.dispose();
        healthBarDrawer.dispose();
    }
}
