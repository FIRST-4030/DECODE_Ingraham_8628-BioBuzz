package org.firstinspires.ftc.teamcode.BehaviorSystem.StateMachine;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.BehaviorSystem.Behavior;

import java.util.function.Supplier;

/**
 * Class used to easily create states that execute one behavior, while passing a state supplier
 * to the getNextState() method to easily define control flow without too much boilerplate.
 * @author Edson James
 */
public class BaseState implements State {
    private final Behavior behavior;
    private final Supplier<State> nextStateSupplier;
    private final Supplier<String> additionalTelemetrySupplier;

    public BaseState(Behavior behavior, Supplier<State> nextStateSupplier) {
        this(behavior, nextStateSupplier, () -> (""));
    }

    public BaseState(Behavior behavior, Supplier<State> nextStateSupplier, Supplier<String> additionalTelemetrySupplier) {
        this.behavior = behavior;
        this.nextStateSupplier = nextStateSupplier;
        this.additionalTelemetrySupplier = additionalTelemetrySupplier;
    }

    @Override
    public void enter() { behavior.enter(); }

    @Override
    public void update() { behavior.update(); }

    @Override
    public boolean isComplete() { return behavior.isComplete(); }

    @Override
    public State getNextState() {
        return nextStateSupplier.get();
    }

    @Override
    public void exit() { behavior.exit(); }

    @Override
    public void processTelemetry(Telemetry telemetry, String prefix) {
        behavior.processTelemetry(telemetry, prefix);
        telemetry.addLine(prefix + additionalTelemetrySupplier.get());
    }

    @Override
    public String getLabel() {
        return behavior.getLabel();
    }
}
