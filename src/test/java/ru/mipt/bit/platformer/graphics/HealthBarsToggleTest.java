package ru.mipt.bit.platformer.graphics;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class HealthBarsToggleTest {

    @Test
    public void startsHidden() {
        assertFalse(new HealthBarsToggle().isVisible());
    }

    @Test
    public void toggleShowsAndHides() {
        HealthBarsToggle toggle = new HealthBarsToggle();

        toggle.toggle();
        assertTrue(toggle.isVisible());

        toggle.toggle();
        assertFalse(toggle.isVisible());
    }
}
