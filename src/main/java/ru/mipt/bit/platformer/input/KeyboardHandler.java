package ru.mipt.bit.platformer.input;

import java.util.ArrayList;
import java.util.List;

public class KeyboardHandler {

    private Keyboard keyboard;
    private List<Binding> bindings = new ArrayList<Binding>();

    public KeyboardHandler(Keyboard keyboard) {
        this.keyboard = keyboard;
    }

    public void bind(Command command, int... keys) {
        bindings.add(new Binding(command, keys, false));
    }

    public void bindOnce(Command command, int... keys) {
        bindings.add(new Binding(command, keys, true));
    }

    public void collect(List<Command> commands) {
        for (Binding binding : bindings) {
            if (isTriggered(binding)) {
                commands.add(binding.command);
            }
        }
    }

    public void handle() {
        List<Command> commands = new ArrayList<Command>();
        collect(commands);
        for (Command command : commands) {
            command.execute();
        }
    }

    private boolean isTriggered(Binding binding) {
        if (binding.once) {
            return wasAnyKeyJustPressed(binding.keys);
        }
        return isAnyKeyPressed(binding.keys);
    }

    private boolean isAnyKeyPressed(int[] keys) {
        for (int key : keys) {
            if (keyboard.isPressed(key)) {
                return true;
            }
        }
        return false;
    }

    private boolean wasAnyKeyJustPressed(int[] keys) {
        for (int key : keys) {
            if (keyboard.wasJustPressed(key)) {
                return true;
            }
        }
        return false;
    }

    private static class Binding {
        private Command command;
        private int[] keys;
        private boolean once;

        private Binding(Command command, int[] keys, boolean once) {
            this.command = command;
            this.keys = keys;
            this.once = once;
        }
    }
}
