package org.firstinspires.ftc.teamcode.BehaviorSystem;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.List;

public class ParallelBehavior implements Behavior {
    public enum CompletionCondition {
        ALL,
        ANY,
        FIRST,
    }

    private final CompletionCondition completionCondition;
    private final List<Behavior> behaviors;
    private final String label;

    public ParallelBehavior(CompletionCondition completionCondition, List<Behavior> behaviors, String label) {
        this.completionCondition = completionCondition;
        this.behaviors = behaviors;
        this.label = label;
    }

    @Override
    public void enter() {
        for (Behavior behavior: behaviors) {
            behavior.enter();
        }
    }

    @Override
    public void update() {
        for (Behavior behavior: behaviors) {
            if (!behavior.isComplete()) {
                behavior.update();
            }
        }
    }

    @Override
    public boolean isComplete() {
        switch (this.completionCondition) {
            case ALL:
                return areAllBehaviorsCompleted();
            case ANY:
                return areAnyBehaviorsCompleted();
            case FIRST:
                return isIndex0Completed();
            default:
                return false;
        }
    }

    @Override
    public void exit() {
        for (Behavior behavior : behaviors) {
            behavior.exit();
        }
    }

    public boolean areAllBehaviorsCompleted() {
        for (Behavior behavior: behaviors) {
            if (!behavior.isComplete()) {
                return false;
            }
        }

        return true;
    }

    public boolean areAnyBehaviorsCompleted() {
        for (Behavior behavior: behaviors) {
            if (behavior.isComplete()) {
                return true;
            }
        }

        return false;
    }

    public boolean isIndex0Completed() {
        if (behaviors.isEmpty()) {
            return true;
        }

        return behaviors.get(0).isComplete();
    }

    @Override
    public void processTelemetry(Telemetry telemetry, String prefix) {
        int currentMS = (int) (System.currentTimeMillis());

        telemetry.addLine(prefix + "(Ends when " + completionCondition + " behavior(s) complete)");
        for (Behavior behavior : behaviors) {
            String listPrefix = "--> ";
            if (currentMS % 500 < -250) {
                listPrefix = "--->";
            }
            if (behavior.isComplete()) {
                listPrefix = "✔   ";
            }
            telemetry.addLine(prefix + listPrefix + behavior.getLabel());

            if (!behavior.isComplete()) {
                behavior.processTelemetry(telemetry, prefix + "    ");
            }
        }
    }

    @Override
    public String getLabel() {
        return label;
    }
}
