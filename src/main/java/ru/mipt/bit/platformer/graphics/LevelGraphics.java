package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import ru.mipt.bit.platformer.model.Bullet;
import ru.mipt.bit.platformer.model.LevelEntity;
import ru.mipt.bit.platformer.model.LevelListener;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;
import ru.mipt.bit.platformer.util.TileMovement;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LevelGraphics implements LevelListener {

    private String tankTexturePath;
    private String treeTexturePath;
    private String bulletTexturePath;
    private float treeRotation;
    private TileMovement tileMovement;
    private TiledMapTileLayer groundLayer;
    private Map<LevelEntity, Drawable> drawables = new LinkedHashMap<LevelEntity, Drawable>();

    public LevelGraphics(
            String tankTexturePath,
            String treeTexturePath,
            String bulletTexturePath,
            float treeRotation,
            TileMovement tileMovement,
            TiledMapTileLayer groundLayer
    ) {
        this.tankTexturePath = tankTexturePath;
        this.treeTexturePath = treeTexturePath;
        this.bulletTexturePath = bulletTexturePath;
        this.treeRotation = treeRotation;
        this.tileMovement = tileMovement;
        this.groundLayer = groundLayer;
    }

    @Override
    public void onObjectAdded(LevelEntity entity) {
        Drawable drawable = createDrawable(entity);
        if (drawable != null) {
            drawables.put(entity, drawable);
        }
    }

    @Override
    public void onObjectRemoved(LevelEntity entity) {
        Drawable drawable = drawables.remove(entity);
        if (drawable != null) {
            drawable.dispose();
        }
    }

    public void draw(Batch batch) {
        for (Drawable drawable : drawables.values()) {
            drawable.draw(batch);
        }
    }

    public void dispose() {
        List<Drawable> snapshot = new ArrayList<Drawable>(drawables.values());
        drawables.clear();
        for (Drawable drawable : snapshot) {
            drawable.dispose();
        }
    }

    private Drawable createDrawable(LevelEntity entity) {
        if (entity instanceof Tank) {
            return new TankGraphics(tankTexturePath, (Tank) entity, tileMovement);
        }
        if (entity instanceof Tree) {
            return new TreeGraphics(treeTexturePath, (Tree) entity, groundLayer, treeRotation);
        }
        if (entity instanceof Bullet) {
            return new BulletGraphics(bulletTexturePath, (Bullet) entity, tileMovement);
        }
        return null;
    }
}
