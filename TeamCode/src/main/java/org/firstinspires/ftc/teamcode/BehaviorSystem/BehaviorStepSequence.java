package org.firstinspires.ftc.teamcode.BehaviorSystem;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class BehaviorStepSequence {
    private final BehaviorStep[] behaviorSteps;
    private int activeStepIndex = 0;

    public BehaviorStepSequence(BehaviorStep[] behaviorSteps) {
        this.behaviorSteps = behaviorSteps;
    }

    public void reset() {
        stop();
        activeStepIndex = 0;
    }

    public void update() {
        if (isFinished()) {
            return;
        }

        BehaviorStep activeBehaviorStep = behaviorSteps[activeStepIndex];

        if (!activeBehaviorStep.isInitialized()) {
            activeBehaviorStep.init();
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
    }

    public void stop() {
        if (isFinished()) { return; };

        BehaviorStep activeBehaviorStep = behaviorSteps[activeStepIndex];
        activeBehaviorStep.exit();

        activeStepIndex = behaviorSteps.length;
    }

    public void processTelemetry(Telemetry telemetry) {
        if (isFinished()) { return; }

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
