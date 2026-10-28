package ru.mipt.bit.platformer.control;

import org.junit.Test;

import static org.junit.Assert.assertNull;

public class PlayerMovementStrategyTest {

    @Test
    public void doesNotChooseDirection() {
        assertNull(new PlayerMovementStrategy().chooseDirection());
    }
}
