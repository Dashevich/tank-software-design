package ru.mipt.bit.platformer.input;

/**
 * Port. Read keys. App does not know LibGDX.
 */
public interface Keyboard {

    boolean isPressed(int keyCode);

    boolean wasJustPressed(int keyCode);
}
