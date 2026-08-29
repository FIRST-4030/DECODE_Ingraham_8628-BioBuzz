package org.firstinspires.ftc.teamcode.BehaviorSystem;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.List;

/**
 * A behavior which runs every behavior in a list on every update call. The behaviors are executed in the order
 * that they appear in the list. The completion condition strategy of parallelBehavior can be specified:
 * ALL for when every behavior is complete, ANY for when any behavior is complete, and FIRST for when the
 * first behavior in the list completes. The BehaviorBuilder uses ALL by default when creating parallelBehaviors.
 * @author Edson James
 * @see SequentialBehavior
 */
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

    /**
     * Enters all behaviors in the list.
     */
    @Override
    public void enter() {
        for (Behavior behavior: behaviors) {
            behavior.enter();
        }
    }

    /**
     * Updates all behaviors in the list that are not completed.
     */
    @Override
    public void update() {
        for (Behavior behavior: behaviors) {
            if (!behavior.isComplete()) {
                behavior.update();
            }
        }
    }

    /**
     * Returns whether the parallel behavior is complete based on the completionCondition strategy:
     * ALL, ANY, or FIRST.
     * @return Whether the parallel behavior is complete
     */
    @Override
    public boolean isComplete() {
        switch (this.completionCondition) {
            case ALL:
                return areAllBehaviorsCompleted();
            case ANY:
                return areAnyBehaviorsCompleted();
            case FIRST:
                return isFirstCompleted();
            default:
                return false;
        }
    }

    /**
     * Exits every behavior in the list.
     */
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

    public boolean isFirstCompleted() {
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
            // There's some crazy jazz here that just makes the arrows look pretty lol
            String listPrefix = "--> ";
            if (currentMS % 500 > 250) {
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
        return "PAR.: " + label;
    }
}
