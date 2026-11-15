package ru.mipt.bit.platformer.graphics;

/**
 * Application. Flag: show HP bars or not.
 */
public class HealthBarsToggle {

    private boolean visible;

    public void toggle() {
        visible = !visible;
    }

    public boolean isVisible() {
        return visible;
    }
}
