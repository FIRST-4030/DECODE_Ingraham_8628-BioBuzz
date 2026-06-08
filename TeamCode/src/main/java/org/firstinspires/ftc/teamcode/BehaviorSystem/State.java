package org.firstinspires.ftc.teamcode.BehaviorSystem;
import java.lang.Runnable;
import java.util.function.Supplier;

public class State {
    private final Runnable enterMethod;
    private final Runnable updateMethod;
    private final Supplier<State> getNextStateOrNullMethod;
    private final Runnable exitMethod;

    public State(Runnable enterMethod, Runnable updateMethod, Supplier<State> getNextStateOrNullMethod, Runnable exitMethod) {
        this.enterMethod = enterMethod;
        this.updateMethod = updateMethod;
        this.getNextStateOrNullMethod = getNextStateOrNullMethod;
        this.exitMethod = exitMethod;
    }

    public void enter() {
        enterMethod.run();
    }

    public void update() {
        updateMethod.run();
    }

    public State getNextStateOrNull() {
        return getNextStateOrNullMethod.get();
    }

    public void exit() {
        exitMethod.run();
    }
}
