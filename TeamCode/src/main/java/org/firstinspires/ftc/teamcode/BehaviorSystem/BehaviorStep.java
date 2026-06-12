package org.firstinspires.ftc.teamcode.BehaviorSystem;

/**
 * Intended for use in one BehaviorStepSequencePerformer only. Takes a primary Behavior and optional secondary
 * behaviors to be performed simultaneously. If using secondary behaviors, the user must specify
 * which of the primary and secondary Behaviors' completed state(s) should be checked against before
 * moving to the next BehaviorStep in a BehaviorStepSequencePerformer.
 * @author edsonjames
 */
public class BehaviorStep {
    public enum StepCompletedConditionType {
        ON_PRIMARY_BEHAVIOR_COMPLETED,
        ON_SECONDARY_BEHAVIORS_COMPLETED,
        ON_ALL_BEHAVIORS_COMPLETED,
        ON_ANY_BEHAVIORS_COMPLETED
    }

    private final StepCompletedConditionType stepCompletedConditionType;
    private final Behavior primaryBehavior;
    private final Behavior[] secondaryBehaviors;

    private boolean initialized = false;

    /**
     * Constructor that takes only one Behavior. This BehaviorStep now only performs this single
     * Behavior and no secondary Behavior(s).
     * @param primaryBehavior The single Behavior to perform.
     */
    public BehaviorStep(Behavior primaryBehavior) {
        this.stepCompletedConditionType = StepCompletedConditionType.ON_PRIMARY_BEHAVIOR_COMPLETED;
        this.primaryBehavior = primaryBehavior;
        this.secondaryBehaviors = new Behavior[0];
    }

    /**
     * Constructor that takes a primary Behavior, array of secondaryBehaviors, and
     * StepCompletedConditionType to specify which Behaviors must complete before the
     * BehaviorStepSequencePerformer can move on to the next step.
     * @param stepCompletedConditionType The StepType to use.
     * @param primaryBehavior The primary Behavior to perform.
     * @param secondaryBehaviors The secondary Behavior(s) to perform.
     */
    public BehaviorStep(StepCompletedConditionType stepCompletedConditionType, Behavior primaryBehavior, Behavior[] secondaryBehaviors) {
        this.stepCompletedConditionType = stepCompletedConditionType;
        this.primaryBehavior = primaryBehavior;
        this.secondaryBehaviors = secondaryBehaviors;
    }

    /**
     * Executes when this BehaviorStep first becomes active. Calls enter() on the primary Behavior
     * and all secondary Behaviors.
     */
    public void init() {
        this.primaryBehavior.enter();
        for (Behavior secondaryBehavior: this.secondaryBehaviors) {
            secondaryBehavior.enter();
        }
        initialized = true;
    }

    /**
     * Executes every frame this BehaviorStep is active. Calls update() on the primary Behavior if
     * it is not complete, and calls update() on all secondary Behaviors that are not complete.
     */
    public void update() {
        if (!this.primaryBehavior.isComplete()) {
            this.primaryBehavior.update();
        }
        for (Behavior secondaryBehavior: this.secondaryBehaviors) {
            if (!secondaryBehavior.isComplete()) {
                secondaryBehavior.update();
            }
        }
    }

    /**
     * Returns whether this BehaviorStep is completed based on the StepCompletedConditionType for
     * this BehaviorStep.
     * <ul>
     *     <li>ON_PRIMARY_BEHAVIOR_COMPLETED: Returns true when the primary Behavior is
     *     complete, ignoring the completion status of all secondary behaviors.</li>
     *     <li>ON_SECONDARY_BEHAVIORS_COMPLETED: Returns true when all the secondary Behavior(s) are
     *     complete, ignoring the completion status of the primary behavior.</li>
     *     <li>ON_ALL_BEHAVIORS_COMPLETED: Returns true when the primary Behavior and
     *     all secondary behaviors complete.</li>
     *     <li>ON_ANY_BEHAVIORS_COMPLETED: Returns true when the primary Behavior or any of the
     *     secondary behaviors complete.</li>
     * </ul>
     * @return Whether the BehaviorStep is complete.
     */
    public boolean isComplete() {
        switch (this.stepCompletedConditionType) {
            case ON_PRIMARY_BEHAVIOR_COMPLETED:
                return (this.primaryBehavior.isComplete());
            case ON_SECONDARY_BEHAVIORS_COMPLETED:
                return secondaryBehaviorsFinished();
            case ON_ALL_BEHAVIORS_COMPLETED:
                return allBehaviorsFinished();
            case ON_ANY_BEHAVIORS_COMPLETED:
                return anyBehaviorsFinished();
            default:
                return false;
        }
    }

    public boolean secondaryBehaviorsFinished() {
        for (Behavior secondaryBehavior: this.secondaryBehaviors) {
            if (!secondaryBehavior.isComplete()) {
                return false;
            }
        }

        return true;
    }

    public boolean allBehaviorsFinished() {
        if (!this.primaryBehavior.isComplete()) {
            return false;
        }

        for (Behavior secondaryBehavior: this.secondaryBehaviors) {
            if (!secondaryBehavior.isComplete()) {
                return false;
            }
        }

        return true;
    }

    public boolean anyBehaviorsFinished() {
        if (this.primaryBehavior.isComplete()) {
            return true;
        }

        for (Behavior secondaryBehavior: this.secondaryBehaviors) {
            if (secondaryBehavior.isComplete()) {
                return true;
            }
        }

        return false;
    }

    /**
    * Executes when this BehaviorStep is no longer active. Calls exit() on the primary Behavior
    * and all secondary Behaviors.
    */

    public void exit() {
        this.primaryBehavior.exit();
        for (Behavior secondaryBehavior: this.secondaryBehaviors) {
            secondaryBehavior.exit();
        }
        initialized = false;
    }

    public Behavior getPrimaryBehavior() {
        return primaryBehavior;
    }

    public Behavior[] getSecondaryBehaviors() {
        return secondaryBehaviors;
    }

    public boolean isInitialized() {
        return initialized;
    }
}
