package org.firstinspires.ftc.teamcode.BehaviorSystem;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class BehaviorStepSequence {
    private final BehaviorStep[] behaviorSteps;
    private int activeStepIndex = 0;
    private boolean activeStepInitialized = false;

    public BehaviorStepSequence(BehaviorStep[] behaviorSteps) {
        this.behaviorSteps = behaviorSteps;
    }

    public void reset() {
        activeStepIndex = 0;
        activeStepInitialized = false;
    }

    public void update() {
        if (isFinished()) {
            return;
        }

        BehaviorStep activeBehaviorStep = behaviorSteps[activeStepIndex];

        if (!activeStepInitialized) {
            activeBehaviorStep.init();
            activeStepInitialized = true;
        }

        activeBehaviorStep.update();

        if (activeBehaviorStep.isFinished()) {
            continueSequence();
        }
    }

    public boolean isFinished() {
        return (activeStepIndex >= behaviorSteps.length);
    }

    public void continueSequence() {
        BehaviorStep activeBehaviorStep = behaviorSteps[activeStepIndex];
        activeBehaviorStep.exit();

        activeStepIndex++;
        activeStepInitialized = false;
    }

    public void stop() {
        BehaviorStep activeBehaviorStep = behaviorSteps[activeStepIndex];
        activeBehaviorStep.exit();

        activeStepIndex = behaviorSteps.length;
        activeStepInitialized = false;
    }

    public void processTelemetry(Telemetry telemetry) {
        BehaviorStep activeBehaviorStep = behaviorSteps[activeStepIndex];

        telemetry.addData("Behavior step sequence is finished", isFinished());
        if (isFinished()) { return; }

        telemetry.addData("Active step initialized", activeStepInitialized);
        if (!activeStepInitialized) { return; }

        telemetry.addData("Active step index", activeStepIndex);
        telemetry.addData("Active step primary behavior", activeBehaviorStep.getPrimaryBehavior());
        telemetry.addData("Active step secondary behaviors", activeBehaviorStep.getSecondaryBehaviors());
        telemetry.addLine();
    }
}
