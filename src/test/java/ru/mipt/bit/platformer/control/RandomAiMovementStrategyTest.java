package ru.mipt.bit.platformer.control;

import org.junit.Test;
import ru.mipt.bit.platformer.model.Direction;

import java.util.EnumSet;
import java.util.Random;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class RandomAiMovementStrategyTest {

    @Test
    public void alwaysChoosesSomeDirection() {
        RandomAiMovementStrategy strategy = new RandomAiMovementStrategy(new Random(1L));

        for (int i = 0; i < 20; i++) {
            assertNotNull(strategy.chooseDirection());
        }
    }

    @Test
    public void sameSeedGivesSameSequence() {
        RandomAiMovementStrategy first = new RandomAiMovementStrategy(new Random(7L));
        RandomAiMovementStrategy second = new RandomAiMovementStrategy(new Random(7L));

        for (int i = 0; i < 10; i++) {
            assertEquals(first.chooseDirection(), second.chooseDirection());
        }
    }

    @Test
    public void canChooseEveryDirection() {
        RandomAiMovementStrategy strategy = new RandomAiMovementStrategy(new Random(0L));
        Set<Direction> seen = EnumSet.noneOf(Direction.class);

        for (int i = 0; i < 100; i++) {
            seen.add(strategy.chooseDirection());
        }

        assertTrue(seen.contains(Direction.UP));
        assertTrue(seen.contains(Direction.DOWN));
        assertTrue(seen.contains(Direction.LEFT));
        assertTrue(seen.contains(Direction.RIGHT));
    }
}
