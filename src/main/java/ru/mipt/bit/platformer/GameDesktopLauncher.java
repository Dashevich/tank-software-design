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
import ru.mipt.bit.platformer.control.ActionStrategy;
import ru.mipt.bit.platformer.control.PlayerActionStrategy;
import ru.mipt.bit.platformer.control.RandomAiActionStrategy;
import ru.mipt.bit.platformer.control.TankController;
import ru.mipt.bit.platformer.graphics.GameFieldGraphics;
import ru.mipt.bit.platformer.graphics.LevelGraphics;
import ru.mipt.bit.platformer.input.Command;
import ru.mipt.bit.platformer.input.GdxKeyboard;
import ru.mipt.bit.platformer.input.KeyboardHandler;
import ru.mipt.bit.platformer.input.MoveCommand;
import ru.mipt.bit.platformer.input.ShootCommand;
import ru.mipt.bit.platformer.level.FileLevelGenerator;
import ru.mipt.bit.platformer.level.Level;
import ru.mipt.bit.platformer.level.LevelGenerator;
import ru.mipt.bit.platformer.level.RandomLevelGenerator;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GameField;
import ru.mipt.bit.platformer.model.LogicalLevel;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;

public class GameDesktopLauncher implements ApplicationListener {

    private static final int TILE_SIZE_PX = 128;
    private static final int WINDOW_WIDTH = GameField.WIDTH * TILE_SIZE_PX;
    private static final int WINDOW_HEIGHT = GameField.HEIGHT * TILE_SIZE_PX;
    private static final float TANK_MOVEMENT_SPEED = 0.4f;
    private static final int TANK_HEALTH = 1;
    private static final int BULLET_DAMAGE = 1;
    private static final float BULLET_SPEED = 0.15f;
    private static final float TREE_ROTATION = 0f;
    private static final float BACKGROUND_BLUE = 0.2f;
    private static final float AI_SHOOT_CHANCE = 0.2f;
    private static final String LEVEL_FILE = "/level.txt";
    private static final String RANDOM_LEVEL_ARG = "random";
    private static final int RANDOM_OBSTACLE_COUNT = 10;
    private static final int AI_TANK_COUNT = 3;

    private LevelGenerator levelGenerator;
    private Batch batch;
    private LogicalLevel logicalLevel;
    private List<TankController> tankControllers;
    private GameFieldGraphics gameFieldGraphics;
    private LevelGraphics levelGraphics;
    private KeyboardHandler keyboardHandler;

    public GameDesktopLauncher(LevelGenerator levelGenerator) {
        this.levelGenerator = levelGenerator;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        gameFieldGraphics = new GameFieldGraphics("level.tmx", batch, Interpolation.smooth);

        logicalLevel = new LogicalLevel(new GameField(GameField.WIDTH, GameField.HEIGHT));
        levelGraphics = new LevelGraphics(
                "images/tank_blue.png",
                "images/greenTree.png",
                "images/brick.png",
                TREE_ROTATION,
                gameFieldGraphics.getTileMovement(),
                gameFieldGraphics.getGroundLayer()
        );
        logicalLevel.addListener(levelGraphics);

        tankControllers = new ArrayList<TankController>();
        Level level = levelGenerator.generate();

        for (GridPoint2 treeCoordinates : level.getTreeCoordinates()) {
            logicalLevel.add(new Tree(treeCoordinates));
        }

        Tank playerTank = addTank(level.getPlayerStart(), new PlayerActionStrategy());
        bindPlayerControls(playerTank);

        Random aiRandom = new Random();
        ActionStrategy aiStrategy = new RandomAiActionStrategy(aiRandom, AI_SHOOT_CHANCE);
        for (GridPoint2 aiStart : level.getAiTankStarts()) {
            addTank(aiStart, aiStrategy);
        }
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0f, 0f, BACKGROUND_BLUE, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        List<Command> commands = new ArrayList<Command>();
        keyboardHandler.collect(commands);
        for (TankController controller : tankControllers) {
            controller.collectAction(commands);
        }

        for (Command command : commands) {
            command.execute();
        }

        logicalLevel.tick(Gdx.graphics.getDeltaTime());
        removeDeadControllers();

        gameFieldGraphics.render();
        batch.begin();
        levelGraphics.draw(batch);
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
        levelGraphics.dispose();
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

    private Tank addTank(GridPoint2 start, ActionStrategy strategy) {
        Tank tank = new Tank(start, TANK_MOVEMENT_SPEED, TANK_HEALTH, BULLET_DAMAGE, BULLET_SPEED);
        logicalLevel.add(tank);
        tankControllers.add(new TankController(tank, strategy, logicalLevel));
        return tank;
    }

    private void bindPlayerControls(Tank playerTank) {
        keyboardHandler = new KeyboardHandler(new GdxKeyboard());
        keyboardHandler.bind(new MoveCommand(playerTank, logicalLevel, Direction.UP), Input.Keys.UP, Input.Keys.W);
        keyboardHandler.bind(new MoveCommand(playerTank, logicalLevel, Direction.LEFT), Input.Keys.LEFT, Input.Keys.A);
        keyboardHandler.bind(new MoveCommand(playerTank, logicalLevel, Direction.DOWN), Input.Keys.DOWN, Input.Keys.S);
        keyboardHandler.bind(new MoveCommand(playerTank, logicalLevel, Direction.RIGHT), Input.Keys.RIGHT, Input.Keys.D);
        keyboardHandler.bind(new ShootCommand(playerTank, logicalLevel), Input.Keys.SPACE);
    }

    private void removeDeadControllers() {
        Iterator<TankController> iterator = tankControllers.iterator();
        while (iterator.hasNext()) {
            TankController controller = iterator.next();
            if (!logicalLevel.contains(controller.getTank())) {
                iterator.remove();
            }
        }
    }
}
