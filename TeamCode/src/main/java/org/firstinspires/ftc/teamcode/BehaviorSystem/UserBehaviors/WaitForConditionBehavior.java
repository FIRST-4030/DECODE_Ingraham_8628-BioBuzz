package org.firstinspires.ftc.teamcode.BehaviorSystem.UserBehaviors;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.BehaviorSystem.Behavior;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

/**
 * Behavior that completes when a given condition (a BooleanSupplier) returns true. Could be used
 * as the primary behavior in a BehaviorStep in conjunction with other behaviors for action to be
 * taken while waiting for a condition before moving to the next BehaviorStep.
 */
public class WaitForConditionBehavior implements Behavior {
    private final BooleanSupplier condition;

    /**
     * Takes a BooleanSupplier that reflects the condition you want to wait for.
     * @param condition The BooleanSupplier that reflects the condition you want to wait for.
     */
    public WaitForConditionBehavior(BooleanSupplier condition) {
        this.condition = condition;
    }

    /**
     * Does nothing.
     */
    @Override
    public void enter() {}

    /**
     * Does nothing.
     */
    @Override
    public void update() {}

    /**
     * Returns whether the condition is true.
     * @return Whether the condition is true.
     */
    @Override
    public boolean isComplete() {
        return condition.getAsBoolean();
    }

    /**
     * Does nothing.
     */
    @Override
    public void exit() {};

    @Override
    public void processTelemetry(Telemetry telemetry, String prefix) {
        telemetry.addLine(prefix + "(Waiting for condition...)");
    }
}
