package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;
import ru.mipt.bit.platformer.graphics.Drawable;
import ru.mipt.bit.platformer.graphics.GameFieldGraphics;
import ru.mipt.bit.platformer.graphics.TankGraphics;
import ru.mipt.bit.platformer.graphics.TreeGraphics;
import ru.mipt.bit.platformer.input.GdxKeyboard;
import ru.mipt.bit.platformer.input.KeyboardHandler;
import ru.mipt.bit.platformer.input.MoveCommand;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GameField;
import ru.mipt.bit.platformer.model.Obstacle;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;

import java.util.ArrayList;
import java.util.List;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;

public class GameDesktopLauncher implements ApplicationListener {

    private static final int TILE_SIZE_PX = 128;
    private static final int WINDOW_WIDTH = GameField.WIDTH * TILE_SIZE_PX;
    private static final int WINDOW_HEIGHT = GameField.HEIGHT * TILE_SIZE_PX;
    private static final int TANK_START_X = 1;
    private static final int TANK_START_Y = 1;
    private static final float TANK_MOVEMENT_SPEED = 0.4f;
    private static final int TREE_X = 1;
    private static final int TREE_Y = 3;
    private static final float TREE_ROTATION = 0f;
    private static final float BACKGROUND_BLUE = 0.2f;

    private Batch batch;
    private GameField gameField;
    private Tank tank;
    private List<Obstacle> obstacles;
    private GameFieldGraphics gameFieldGraphics;
    private List<Drawable> drawables;
    private KeyboardHandler keyboardHandler;

    @Override
    public void create() {
        batch = new SpriteBatch();

        gameField = new GameField(GameField.WIDTH, GameField.HEIGHT);
        gameFieldGraphics = new GameFieldGraphics("level.tmx", batch, Interpolation.smooth);
        tank = new Tank(new GridPoint2(TANK_START_X, TANK_START_Y), TANK_MOVEMENT_SPEED);

        obstacles = new ArrayList<Obstacle>();
        drawables = new ArrayList<Drawable>();
        drawables.add(new TankGraphics("images/tank_blue.png", tank, gameFieldGraphics.getTileMovement()));
        addTree(new GridPoint2(TREE_X, TREE_Y));

        keyboardHandler = new KeyboardHandler(new GdxKeyboard());
        keyboardHandler.bind(new MoveCommand(tank, gameField, obstacles, Direction.UP), Input.Keys.UP, Input.Keys.W);
        keyboardHandler.bind(new MoveCommand(tank, gameField, obstacles, Direction.LEFT), Input.Keys.LEFT, Input.Keys.A);
        keyboardHandler.bind(new MoveCommand(tank, gameField, obstacles, Direction.DOWN), Input.Keys.DOWN, Input.Keys.S);
        keyboardHandler.bind(new MoveCommand(tank, gameField, obstacles, Direction.RIGHT), Input.Keys.RIGHT, Input.Keys.D);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0f, 0f, BACKGROUND_BLUE, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        keyboardHandler.handle();
        tank.update(Gdx.graphics.getDeltaTime());

        gameFieldGraphics.render();
        batch.begin();
        for (Drawable drawable : drawables) {
            drawable.draw(batch);
        }
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        // do not react to window resizing
    }

    @Override
    public void pause() {
        // game doesn't get paused
    }

    @Override
    public void resume() {
        // game doesn't get paused
    }

    @Override
    public void dispose() {
        for (Drawable drawable : drawables) {
            drawable.dispose();
        }
        gameFieldGraphics.dispose();
        batch.dispose();
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setWindowedMode(WINDOW_WIDTH, WINDOW_HEIGHT);
        new Lwjgl3Application(new GameDesktopLauncher(), config);
    }

    private void addTree(GridPoint2 coordinates) {
        Tree tree = new Tree(coordinates);
        obstacles.add(tree);
        drawables.add(new TreeGraphics(
                "images/greenTree.png",
                tree,
                gameFieldGraphics.getGroundLayer(),
                TREE_ROTATION
        ));
    }
}
