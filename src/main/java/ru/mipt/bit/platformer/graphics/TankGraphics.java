package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.model.Movable;
import ru.mipt.bit.platformer.util.TileMovement;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;
import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;

public class TankGraphics implements Drawable {

    private Texture texture;
    private TextureRegion graphics;
    private Rectangle rectangle;
    private Movable movable;
    private TileMovement tileMovement;

    public TankGraphics(String texturePath, Movable movable, TileMovement tileMovement) {
        texture = new Texture(texturePath);
        graphics = new TextureRegion(texture);
        rectangle = createBoundingRectangle(graphics);
        this.movable = movable;
        this.tileMovement = tileMovement;
    }

    @Override
    public void draw(Batch batch) {
        tileMovement.moveRectangleBetweenTileCenters(
                rectangle,
                movable.getCoordinates(),
                movable.getDestinationCoordinates(),
                movable.getMovementProgress()
        );
        drawTextureRegionUnscaled(batch, graphics, rectangle, movable.getRotation());
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}
