package org.firstinspires.ftc.teamcode.BehaviorSystem.UserBehaviors;

import org.firstinspires.ftc.robotcore.external.Supplier;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.BehaviorSystem.Behavior;

public class InstantBehavior implements Behavior {

    private final String label;
    private final Runnable actionRunnable;

    public InstantBehavior(
            Runnable actionRunnable
    ) {
        this(
                actionRunnable,
                "Instant Behavior"
        );
    }

    public InstantBehavior(
            Runnable actionRunnable,
            String label
    ) {
        this.actionRunnable = actionRunnable;
        this.label = label;
    }

    @Override
    public void enter() {
        actionRunnable.run();
    }

    @Override
    public void update() {}

    @Override
    public boolean isComplete() {
        return true;
    }

    @Override
    public void exit() {}

    @Override
    public String getLabel() {
        return label;
    }

    // There's nothing to print because this behavior is never active for more than a frame
    @Override
    public void processTelemetry(Telemetry telemetry, String prefix) {}
}
