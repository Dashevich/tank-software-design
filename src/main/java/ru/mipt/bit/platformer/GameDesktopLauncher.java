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
import ru.mipt.bit.platformer.control.MovementStrategy;
import ru.mipt.bit.platformer.control.PlayerMovementStrategy;
import ru.mipt.bit.platformer.control.RandomAiMovementStrategy;
import ru.mipt.bit.platformer.control.TankController;
import ru.mipt.bit.platformer.graphics.Drawable;
import ru.mipt.bit.platformer.graphics.GameFieldGraphics;
import ru.mipt.bit.platformer.graphics.TankGraphics;
import ru.mipt.bit.platformer.graphics.TreeGraphics;
import ru.mipt.bit.platformer.input.GdxKeyboard;
import ru.mipt.bit.platformer.input.KeyboardHandler;
import ru.mipt.bit.platformer.input.MoveCommand;
import ru.mipt.bit.platformer.level.FileLevelGenerator;
import ru.mipt.bit.platformer.level.Level;
import ru.mipt.bit.platformer.level.LevelGenerator;
import ru.mipt.bit.platformer.level.RandomLevelGenerator;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GameField;
import ru.mipt.bit.platformer.model.Obstacle;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;

public class GameDesktopLauncher implements ApplicationListener {

    private static final int TILE_SIZE_PX = 128;
    private static final int WINDOW_WIDTH = GameField.WIDTH * TILE_SIZE_PX;
    private static final int WINDOW_HEIGHT = GameField.HEIGHT * TILE_SIZE_PX;
    private static final float TANK_MOVEMENT_SPEED = 0.4f;
    private static final float TREE_ROTATION = 0f;
    private static final float BACKGROUND_BLUE = 0.2f;
    private static final String LEVEL_FILE = "/level.txt";
    private static final String RANDOM_LEVEL_ARG = "random";
    private static final int RANDOM_OBSTACLE_COUNT = 10;
    private static final int AI_TANK_COUNT = 3;

    private LevelGenerator levelGenerator;
    private Batch batch;
    private GameField gameField;
    private List<Obstacle> obstacles;
    private List<TankController> tankControllers;
    private GameFieldGraphics gameFieldGraphics;
    private List<Drawable> drawables;
    private KeyboardHandler keyboardHandler;

    public GameDesktopLauncher(LevelGenerator levelGenerator) {
        this.levelGenerator = levelGenerator;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();

        gameField = new GameField(GameField.WIDTH, GameField.HEIGHT);
        gameFieldGraphics = new GameFieldGraphics("level.tmx", batch, Interpolation.smooth);

        Level level = levelGenerator.generate();
        obstacles = new ArrayList<Obstacle>();
        drawables = new ArrayList<Drawable>();
        tankControllers = new ArrayList<TankController>();

        for (GridPoint2 treeCoordinates : level.getTreeCoordinates()) {
            addTree(treeCoordinates);
        }

        Tank playerTank = addTank(level.getPlayerStart(), new PlayerMovementStrategy());
        bindPlayerControls(playerTank);

        Random aiRandom = new Random();
        for (GridPoint2 aiStart : level.getAiTankStarts()) {
            addTank(aiStart, new RandomAiMovementStrategy(aiRandom));
        }
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0f, 0f, BACKGROUND_BLUE, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        keyboardHandler.handle();
        float deltaTime = Gdx.graphics.getDeltaTime();
        for (TankController controller : tankControllers) {
            controller.act();
            controller.update(deltaTime);
        }

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
        new Lwjgl3Application(new GameDesktopLauncher(createLevelGenerator(args)), config);
    }

    private static LevelGenerator createLevelGenerator(String[] args) {
        Random random = new Random();
        if (args.length > 0 && RANDOM_LEVEL_ARG.equals(args[0])) {
            return new RandomLevelGenerator(
                    GameField.WIDTH,
                    GameField.HEIGHT,
                    RANDOM_OBSTACLE_COUNT,
                    AI_TANK_COUNT,
                    random
            );
        }
        return new FileLevelGenerator(
                new InputStreamReader(
                        GameDesktopLauncher.class.getResourceAsStream(LEVEL_FILE),
                        StandardCharsets.UTF_8
                ),
                GameField.WIDTH,
                GameField.HEIGHT,
                AI_TANK_COUNT,
                random
        );
    }

    private Tank addTank(GridPoint2 start, MovementStrategy strategy) {
        Tank tank = new Tank(start, TANK_MOVEMENT_SPEED);
        obstacles.add(tank);
        drawables.add(new TankGraphics("images/tank_blue.png", tank, gameFieldGraphics.getTileMovement()));
        tankControllers.add(new TankController(tank, strategy, gameField, obstacles));
        return tank;
    }

    private void bindPlayerControls(Tank playerTank) {
        keyboardHandler = new KeyboardHandler(new GdxKeyboard());
        keyboardHandler.bind(new MoveCommand(playerTank, gameField, obstacles, Direction.UP), Input.Keys.UP, Input.Keys.W);
        keyboardHandler.bind(new MoveCommand(playerTank, gameField, obstacles, Direction.LEFT), Input.Keys.LEFT, Input.Keys.A);
        keyboardHandler.bind(new MoveCommand(playerTank, gameField, obstacles, Direction.DOWN), Input.Keys.DOWN, Input.Keys.S);
        keyboardHandler.bind(new MoveCommand(playerTank, gameField, obstacles, Direction.RIGHT), Input.Keys.RIGHT, Input.Keys.D);
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
