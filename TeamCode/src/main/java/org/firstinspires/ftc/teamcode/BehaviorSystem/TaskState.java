package org.firstinspires.ftc.teamcode.BehaviorSystem;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.function.Supplier;

public class TaskState implements State {
    private final Behavior behavior;
    private final Supplier<State> nextStateSupplier;

    public TaskState(Behavior behavior, Supplier<State> nextStateSupplier) {
        this.behavior = behavior;
        this.nextStateSupplier = nextStateSupplier;
    }

    @Override
    public void enter() { behavior.enter(); }

    @Override
    public void update() { behavior.update(); }

    @Override
    public boolean isComplete() { return behavior.isComplete(); }

    @Override
    public State getNextState() {
        if (behavior.isComplete()) return nextStateSupplier.get();
        return this;
    }

    @Override
    public void exit() { behavior.exit(); }

    @Override
    public void processTelemetry(Telemetry telemetry, String prefix) {
        behavior.processTelemetry(telemetry, prefix);
    }
}
