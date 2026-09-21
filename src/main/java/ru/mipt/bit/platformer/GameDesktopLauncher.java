package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.graphics.GameFieldGraphics;
import ru.mipt.bit.platformer.graphics.TankGraphics;
import ru.mipt.bit.platformer.graphics.TreeGraphics;
import ru.mipt.bit.platformer.input.GdxKeyboard;
import ru.mipt.bit.platformer.input.KeyboardHandler;
import ru.mipt.bit.platformer.input.MoveCommand;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GameField;
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
    private static final int TREE_X = 1;
    private static final int TREE_Y = 3;
    private static final float BACKGROUND_BLUE = 0.2f;

    private Batch batch;
    private GameField gameField;
    private Tank tank;
    private List<Tree> trees;
    private GameFieldGraphics gameFieldGraphics;
    private TankGraphics tankGraphics;
    private List<TreeGraphics> treeGraphicsList;
    private KeyboardHandler keyboardHandler;

    @Override
    public void create() {
        batch = new SpriteBatch();

        gameField = new GameField(GameField.WIDTH, GameField.HEIGHT);
        gameFieldGraphics = new GameFieldGraphics("level.tmx", batch);
        tank = new Tank(new GridPoint2(TANK_START_X, TANK_START_Y));
        tankGraphics = new TankGraphics("images/tank_blue.png", tank);

        trees = new ArrayList<Tree>();
        treeGraphicsList = new ArrayList<TreeGraphics>();
        addTree(new GridPoint2(TREE_X, TREE_Y));

        keyboardHandler = new KeyboardHandler(new GdxKeyboard());
        keyboardHandler.bind(new MoveCommand(tank, gameField, trees, Direction.UP), Input.Keys.UP, Input.Keys.W);
        keyboardHandler.bind(new MoveCommand(tank, gameField, trees, Direction.LEFT), Input.Keys.LEFT, Input.Keys.A);
        keyboardHandler.bind(new MoveCommand(tank, gameField, trees, Direction.DOWN), Input.Keys.DOWN, Input.Keys.S);
        keyboardHandler.bind(new MoveCommand(tank, gameField, trees, Direction.RIGHT), Input.Keys.RIGHT, Input.Keys.D);
        // later: keyboardHandler.bind(new ShootCommand(tank), Input.Keys.SPACE);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0f, 0f, BACKGROUND_BLUE, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        keyboardHandler.handle();
        tank.update(Gdx.graphics.getDeltaTime());

        gameFieldGraphics.render();
        batch.begin();
        tankGraphics.draw(batch, gameFieldGraphics.getTileMovement());
        for (TreeGraphics treeGraphics : treeGraphicsList) {
            treeGraphics.draw(batch);
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
        for (TreeGraphics treeGraphics : treeGraphicsList) {
            treeGraphics.dispose();
        }
        tankGraphics.dispose();
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
        trees.add(tree);
        treeGraphicsList.add(new TreeGraphics(
                "images/greenTree.png",
                tree,
                gameFieldGraphics.getGroundLayer()
        ));
    }
}
