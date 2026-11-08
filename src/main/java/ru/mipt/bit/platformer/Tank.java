package ru.mipt.bit.platformer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.util.TileMovement;

import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.continueProgress;
import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;
import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;

public class Tank {

    private static final float MOVEMENT_SPEED = 0.4f;

    private Texture texture;
    private TextureRegion graphics;
    private Rectangle rectangle;
    private GridPoint2 coordinates;
    private GridPoint2 destinationCoordinates;
    private float movementProgress = 1f;
    private float rotation;

    public Tank(String texturePath, GridPoint2 startCoordinates) {
        texture = new Texture(texturePath);
        graphics = new TextureRegion(texture);
        rectangle = createBoundingRectangle(graphics);
        destinationCoordinates = new GridPoint2(startCoordinates);
        coordinates = new GridPoint2(destinationCoordinates);
        rotation = 0f;
    }

    public boolean hasFinishedMovement() {
        return isEqual(movementProgress, 1f);
    }

    public void tryMove(Direction direction, Tree tree) {
        if (!hasFinishedMovement()) {
            return;
        }
        rotation = direction.getRotation();
        GridPoint2 nextCoordinates = direction.nextCoordinates(coordinates);
        if (!tree.occupies(nextCoordinates)) {
            destinationCoordinates.set(nextCoordinates);
            movementProgress = 0f;
        }
    }

    public void update(float deltaTime, TileMovement tileMovement) {
        tileMovement.moveRectangleBetweenTileCenters(
                rectangle, coordinates, destinationCoordinates, movementProgress);
        movementProgress = continueProgress(movementProgress, deltaTime, MOVEMENT_SPEED);
        if (hasFinishedMovement()) {
            coordinates.set(destinationCoordinates);
        }
    }

    public void draw(Batch batch) {
        drawTextureRegionUnscaled(batch, graphics, rectangle, rotation);
    }

    public void dispose() {
        texture.dispose();
    }
}
