package org.firstinspires.ftc.teamcode.BehaviorSystem;

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
        if (activeStepIndex >= behaviorSteps.length) {
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
}
