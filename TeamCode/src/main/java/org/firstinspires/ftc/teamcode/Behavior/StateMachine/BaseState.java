package org.firstinspires.ftc.teamcode.Behavior.StateMachine;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Behavior.Behavior;

import java.util.function.Supplier;

public class BaseState implements State {
    private final Behavior behavior;
    private final Supplier<State> nextStateSupplier;
    private final Supplier<String> additionalTelemetrySupplier;

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
        return "BaseState: " + behavior.getLabel();
    }
}
