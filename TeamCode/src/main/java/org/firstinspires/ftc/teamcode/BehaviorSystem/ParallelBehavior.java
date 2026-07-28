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

    public ParallelBehavior(CompletionCondition completionCondition, List<Behavior> behaviors) {
        this.completionCondition = completionCondition;
        this.behaviors = behaviors;
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
            if (!behavior.isComplete()) {
                behavior.exit();
            }
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

    }
}
