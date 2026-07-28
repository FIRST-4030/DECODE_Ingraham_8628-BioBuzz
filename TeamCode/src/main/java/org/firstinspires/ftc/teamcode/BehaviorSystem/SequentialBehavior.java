package org.firstinspires.ftc.teamcode.BehaviorSystem;

import android.os.Build;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.List;

public class SequentialBehavior implements Behavior {
    private final List<Behavior> behaviors;
    private int activeBehaviorIndex = 0;
    private final String label;

    public SequentialBehavior(List<Behavior> behaviors, String label) {
        this.behaviors = behaviors;
        this.label = label;
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
        int currentMS = (int) (System.currentTimeMillis());

        for (Behavior behavior: behaviors) {
            String listPrefix = "-     ";
            if (getActiveBehavior() == behavior) {
                if (currentMS % 500 < -250) {
                    listPrefix = "--->";
                } else {
                    listPrefix = "--> ";
                }
            }
            if (behavior.isComplete()) {
                listPrefix = "✔   ";
            }
            telemetry.addLine(prefix + listPrefix + behavior.getClass().getSimpleName());

            if (getActiveBehavior() == behavior) {
                behavior.processTelemetry(telemetry, prefix + "    ");
            }
        }
    }

    @Override
    public String getLabel() {
        return label;
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
