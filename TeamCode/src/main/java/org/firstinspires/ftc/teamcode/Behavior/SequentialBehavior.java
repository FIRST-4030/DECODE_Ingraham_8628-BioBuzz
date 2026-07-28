package org.firstinspires.ftc.teamcode.Behavior;

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
    public void exit() {
        activeBehaviorIndex = 0;
    }

    @Override
    public void processTelemetry(Telemetry telemetry, String prefix) {
        int currentMS = (int) (System.currentTimeMillis());

        for (int i = 0; i < behaviors.size(); i ++) {
            Behavior behavior = behaviors.get(i);

            String listPrefix = "-     ";
            if (getActiveBehavior() == behavior) {
                if (currentMS % 500 < -250) {
                    listPrefix = "--->";
                } else {
                    listPrefix = "--> ";
                }
            }
            if (behavior.isComplete() || activeBehaviorIndex > i) {
                listPrefix = "✔   ";
            }
            telemetry.addLine(prefix + listPrefix + behavior.getLabel());

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
