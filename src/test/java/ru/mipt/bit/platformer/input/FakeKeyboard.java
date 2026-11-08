package ru.mipt.bit.platformer.input;

import java.util.HashSet;
import java.util.Set;

public class FakeKeyboard implements Keyboard {

    private Set<Integer> pressedKeys = new HashSet<Integer>();

    public void press(int keyCode) {
        pressedKeys.add(keyCode);
    }

    public void release(int keyCode) {
        pressedKeys.remove(keyCode);
    }

    @Override
    public boolean isPressed(int keyCode) {
        return pressedKeys.contains(keyCode);
    }
}
