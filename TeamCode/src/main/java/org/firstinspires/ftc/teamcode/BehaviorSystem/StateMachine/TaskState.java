package org.firstinspires.ftc.teamcode.BehaviorSystem.StateMachine;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.BehaviorSystem.Behavior;

import java.util.function.Supplier;

/**
 * Class used to easily create states that execute one behavior and then automatically switch to a
 * specified state once that behavior completes, without too much boilerplate.
 * @author Edson James
 */
public class TaskState implements State {
    private final Behavior behavior;
    private final Supplier<State> onCompleteNextStateSupplier;

    public TaskState(Behavior behavior, Supplier<State> onCompleteNextStateSupplier) {
        this.behavior = behavior;
        this.onCompleteNextStateSupplier = onCompleteNextStateSupplier;
    }

    @Override
    public void enter() { behavior.enter(); }

    @Override
    public void update() { behavior.update(); }

    @Override
    public boolean isComplete() { return behavior.isComplete(); }

    @Override
    public State getNextState() {
        if (behavior.isComplete()) return onCompleteNextStateSupplier.get();
        return this;
    }

    @Override
    public void exit() { behavior.exit(); }

    @Override
    public void processTelemetry(Telemetry telemetry, String prefix) {
        behavior.processTelemetry(telemetry, prefix);
    }

    @Override
    public String getLabel() {
        return behavior.getLabel();
    }
}
