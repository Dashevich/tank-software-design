package ru.mipt.bit.platformer.input;

import org.junit.Test;
import ru.mipt.bit.platformer.graphics.HealthBarsToggle;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ToggleHealthBarsCommandTest {

    @Test
    public void executeTogglesVisibility() {
        HealthBarsToggle toggle = new HealthBarsToggle();
        ToggleHealthBarsCommand command = new ToggleHealthBarsCommand(toggle);

        command.execute();
        assertTrue(toggle.isVisible());

        command.execute();
        assertFalse(toggle.isVisible());
    }
}
