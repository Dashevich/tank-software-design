package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.model.Direction;

import java.util.Random;

public class RandomAiMovementStrategy implements MovementStrategy {

    private static final Direction[] DIRECTIONS = Direction.values();

    private Random random;

    public RandomAiMovementStrategy(Random random) {
        this.random = random;
    }

    @Override
    public Direction chooseDirection() {
        return DIRECTIONS[random.nextInt(DIRECTIONS.length)];
    }
}
