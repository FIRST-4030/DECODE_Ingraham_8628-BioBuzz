package org.firstinspires.ftc.teamcode.BehaviorSystem;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Performs a sequence of BehaviorSteps.
 */
public class BehaviorStepSequencePerformer {
    private final BehaviorStep[] behaviorSteps;
    private int activeStepIndex = 0;

    public BehaviorStepSequencePerformer(BehaviorStep[] behaviorSteps) {
        this.behaviorSteps = behaviorSteps;
    }

    /**
     * Sets the activeStepIndex to 0 and exits the active BehaviorStep if the sequence is being
     * interrupted.
     */
    public void reset() {
        stop();
        activeStepIndex = 0;
    }

    /**
     * Updates the active BehaviorStep. Initializes the active BehaviorStep if it has not been
     * initialized already. Continues to the next BehaviorStep in the sequence if the active
     * BehaviorStep is complete. Does nothing if the sequence is complete.
     */
    public void update() {
        if (isComplete()) {
            return;
        }

        BehaviorStep activeBehaviorStep = behaviorSteps[activeStepIndex];

        if (!activeBehaviorStep.isInitialized()) {
            activeBehaviorStep.init();
        }

        activeBehaviorStep.update();

        if (activeBehaviorStep.isComplete()) {
            continueSequence();
        }
    }

    /**
     * Returns true if the activeStepIndex is equal to or above the number of BehaviorSteps in the
     * sequence.
     * @return Whether the sequence is complete.
     */
    public boolean isComplete() {
        return (activeStepIndex >= behaviorSteps.length);
    }

    /**
     * Exits the current BehaviorStep and increments the activeStepIndex up by one.
     */
    public void continueSequence() {
        BehaviorStep activeBehaviorStep = behaviorSteps[activeStepIndex];
        activeBehaviorStep.exit();

        activeStepIndex++;
    }

    /**
     * Exits the current BehaviorStep and sets the activeStepIndex to the number of BehaviorSteps in
     * the sequence. Does nothing if the sequence is complete.
     */
    public void stop() {
        if (isComplete()) { return; };

        BehaviorStep activeBehaviorStep = behaviorSteps[activeStepIndex];
        activeBehaviorStep.exit();

        activeStepIndex = behaviorSteps.length;
    }

    /**
     * Adds lines of information about the BehaviorStepSequencePerformer, including the active
     * step, whether it is initialized, its index, and active and secondary behavior information.
     * @param telemetry The Telemetry instance to print to.
     */
    public void processTelemetry(Telemetry telemetry) {
        if (isComplete()) { return; }

        BehaviorStep activeBehaviorStep = behaviorSteps[activeStepIndex];

        telemetry.addData("Active step initialized", activeBehaviorStep.isInitialized());
        if (!activeBehaviorStep.isInitialized()) { return; }

        telemetry.addData("Active step index", activeStepIndex);

        telemetry.addData("Active step primary behavior", activeBehaviorStep.getPrimaryBehavior().getClass().getSimpleName());
        activeBehaviorStep.getPrimaryBehavior().processTelemetry(telemetry, "  ");

        if (activeBehaviorStep.getSecondaryBehaviors().length > 0) {
            telemetry.addLine("Active step secondary behaviors:");
            for (Behavior secondaryBehavior : activeBehaviorStep.getSecondaryBehaviors()) {
                telemetry.addLine("   -> " + secondaryBehavior.getClass().getSimpleName());
                secondaryBehavior.processTelemetry(telemetry, "        ");
            }
        }
        telemetry.addLine();
    }
}
