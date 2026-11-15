package ru.mipt.bit.platformer.input;

import com.badlogic.gdx.Gdx;

public class GdxKeyboard implements Keyboard {

    @Override
    public boolean isPressed(int keyCode) {
        return Gdx.input.isKeyPressed(keyCode);
    }

    @Override
    public boolean wasJustPressed(int keyCode) {
        return Gdx.input.isKeyJustPressed(keyCode);
    }
}
