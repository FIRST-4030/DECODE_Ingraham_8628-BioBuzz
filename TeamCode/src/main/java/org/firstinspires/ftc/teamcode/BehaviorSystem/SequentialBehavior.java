package org.firstinspires.ftc.teamcode.BehaviorSystem;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.List;

public class SequentialBehavior implements Behavior {
    private final List<Behavior> behaviors;
    private int activeBehaviorIndex = 0;

    public SequentialBehavior(List<Behavior> behaviors) {
        this.behaviors = behaviors;
    }

    @Override
    public void enter() {
        activeBehaviorIndex = 0;

        if (getActiveBehavior() != null) {
            getActiveBehavior().enter();
        }
    }

    @Override
    public void update() {
        if (isComplete()) { return; }
        if (getActiveBehavior() == null) { return; }

        getActiveBehavior().update();

        if (getActiveBehavior().isComplete()) {
            nextBehavior();
        }
    }

    @Override
    public boolean isComplete() {
        return ( activeBehaviorIndex >= behaviors.size() );
    }

    @Override
    public void exit() {}

    @Override
    public void processTelemetry(Telemetry telemetry, String prefix) {
        for (Behavior behavior: behaviors) {
            String listPrefix = "|  ";
            if (getActiveBehavior() == behavior) {
                listPrefix = "|->";
            }
            telemetry.addLine(prefix + listPrefix + behavior.getClass().getSimpleName());
        }
    }

    private void nextBehavior() {
        getActiveBehavior().exit();
        activeBehaviorIndex += 1;

        if (isComplete()) { return; }

        getActiveBehavior().enter();
    }

    private Behavior getActiveBehavior() {
        Behavior activeBehavior;

        try {
            activeBehavior = behaviors.get(activeBehaviorIndex);
        } catch(Exception ignored) {
            activeBehavior = null;
        }
        return activeBehavior;
    }
}
