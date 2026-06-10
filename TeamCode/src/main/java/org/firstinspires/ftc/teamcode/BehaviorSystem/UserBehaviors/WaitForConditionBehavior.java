package org.firstinspires.ftc.teamcode.BehaviorSystem.UserBehaviors;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.BehaviorSystem.Behavior;

import java.util.function.Supplier;

public class WaitForConditionBehavior implements Behavior {
    private final Supplier<Boolean> condition;

    public WaitForConditionBehavior(Supplier<Boolean> condition) {
        this.condition = condition;
    }

    @Override
    public void enter() {}

    @Override
    public void update() {}


    @Override
    public boolean isFinished() {
        return condition.get();
    }

    @Override
    public void exit() {};

    @Override
    public void processTelemetry(Telemetry telemetry, String prefix) {
        telemetry.addLine(prefix + "(Waiting for condition...)");
    }
}
