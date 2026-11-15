package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LogicalLevelTest {

    @Test
    public void notifiesListenersOnAddAndRemove() {
        LogicalLevel level = new LogicalLevel(new GameField(GameField.WIDTH, GameField.HEIGHT));
        RecordingListener listener = new RecordingListener();
        level.addListener(listener);
        Tree tree = new Tree(new GridPoint2(1, 1));

        level.add(tree);
        level.remove(tree);

        assertEquals(1, listener.added.size());
        assertEquals(tree, listener.added.get(0));
        assertEquals(1, listener.removed.size());
        assertEquals(tree, listener.removed.get(0));
    }

    @Test
    public void tickCallsLiveOnEntities() {
        LogicalLevel level = new LogicalLevel(new GameField(GameField.WIDTH, GameField.HEIGHT));
        CountingEntity entity = new CountingEntity();
        level.add(entity);

        level.tick(0.1f);
        level.tick(0.2f);

        assertEquals(2, entity.calls);
        assertEquals(0.2f, entity.lastDelta, 0.000001f);
    }

    @Test
    public void entitiesAtFindsOccupyingObjects() {
        LogicalLevel level = new LogicalLevel(new GameField(GameField.WIDTH, GameField.HEIGHT));
        Tree tree = new Tree(new GridPoint2(2, 3));
        level.add(tree);

        assertTrue(level.entitiesAt(new GridPoint2(2, 3)).contains(tree));
        assertTrue(level.entitiesAt(new GridPoint2(0, 0)).isEmpty());
    }

    @Test
    public void getObstaclesReturnsOnlyObstacles() {
        LogicalLevel level = new LogicalLevel(new GameField(GameField.WIDTH, GameField.HEIGHT));
        Tree tree = new Tree(new GridPoint2(1, 1));
        CountingEntity plain = new CountingEntity();
        level.add(tree);
        level.add(plain);

        assertEquals(1, level.getObstacles().size());
        assertTrue(level.getObstacles().contains(tree));
        assertFalse(level.contains(plain) && level.getObstacles().contains(plain));
    }

    private static class RecordingListener implements LevelListener {
        private List<LevelEntity> added = new ArrayList<LevelEntity>();
        private List<LevelEntity> removed = new ArrayList<LevelEntity>();

        @Override
        public void onObjectAdded(LevelEntity entity) {
            added.add(entity);
        }

        @Override
        public void onObjectRemoved(LevelEntity entity) {
            removed.add(entity);
        }
    }

    private static class CountingEntity implements LevelEntity {
        private int calls;
        private float lastDelta;

        @Override
        public void live(float deltaTime) {
            calls++;
            lastDelta = deltaTime;
        }
    }
}
