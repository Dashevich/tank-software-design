package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.model.HasHealth;

public class TextureHealthBarDrawer implements HealthBarDrawer {

    private static final float BAR_HEIGHT = 8f;
    private static final float BAR_OFFSET_Y = 12f;
    private static final float BAR_PADDING_X = 8f;

    private Texture pixel;

    @Override
    public void draw(Batch batch, Rectangle bounds, HasHealth hasHealth) {
        float maxHealth = hasHealth.getMaxHealth();
        float ratio = maxHealth <= 0 ? 0f : (float) hasHealth.getHealth() / maxHealth;
        if (ratio < 0f) {
            ratio = 0f;
        }
        if (ratio > 1f) {
            ratio = 1f;
        }

        float barWidth = Math.max(0f, bounds.width - 2f * BAR_PADDING_X);
        float x = bounds.x + BAR_PADDING_X;
        float y = bounds.y + bounds.height + BAR_OFFSET_Y;
        Texture pixelTexture = pixelTexture();
        Color previous = batch.getColor();

        batch.setColor(Color.DARK_GRAY);
        batch.draw(pixelTexture, x, y, barWidth, BAR_HEIGHT);
        batch.setColor(healthColor(ratio));
        batch.draw(pixelTexture, x, y, barWidth * ratio, BAR_HEIGHT);
        batch.setColor(previous);
    }

    @Override
    public void dispose() {
        if (pixel != null) {
            pixel.dispose();
            pixel = null;
        }
    }

    private Color healthColor(float ratio) {
        if (ratio > 0.5f) {
            return Color.GREEN;
        }
        if (ratio > 0.25f) {
            return Color.ORANGE;
        }
        return Color.RED;
    }

    private Texture pixelTexture() {
        if (pixel == null) {
            Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            pixmap.setColor(Color.WHITE);
            pixmap.fill();
            pixel = new Texture(pixmap);
            pixmap.dispose();
        }
        return pixel;
    }
}
