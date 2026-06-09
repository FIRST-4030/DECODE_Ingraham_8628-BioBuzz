package org.firstinspires.ftc.teamcode.BehaviorSystem;

import java.util.function.Supplier;

public class State {
    private final Runnable enterMethod;
    private final Runnable updateMethod;
    private final Supplier<State> getNextState;
    private final Runnable exitMethod;

    public State(Runnable enterMethod, Runnable updateMethod, Supplier<State> getNextState, Runnable exitMethod) {
        this.enterMethod = enterMethod;
        this.updateMethod = updateMethod;
        this.getNextState = getNextState;
        this.exitMethod = exitMethod;
    }

    public void enter() {
        enterMethod.run();
    }

    public void update() {
        updateMethod.run();
    }

    public State getNextState() {
        return getNextState.get();
    }

    public void exit() {
        exitMethod.run();
    }
}
