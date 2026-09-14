package ru.mipt.bit.platformer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;
import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;
import static ru.mipt.bit.platformer.util.GdxGameUtils.moveRectangleAtTileCenter;

public class Tree {

    private Texture texture;
    private TextureRegion graphics;
    private GridPoint2 coordinates;
    private Rectangle rectangle;

    public Tree(String texturePath, GridPoint2 coordinates, TiledMapTileLayer groundLayer) {
        texture = new Texture(texturePath);
        graphics = new TextureRegion(texture);
        this.coordinates = new GridPoint2(coordinates);
        rectangle = createBoundingRectangle(graphics);
        moveRectangleAtTileCenter(groundLayer, rectangle, this.coordinates);
    }

    public boolean occupies(GridPoint2 point) {
        return coordinates.equals(point);
    }

    public void draw(Batch batch) {
        drawTextureRegionUnscaled(batch, graphics, rectangle, 0f);
    }

    public void dispose() {
        texture.dispose();
    }
}
