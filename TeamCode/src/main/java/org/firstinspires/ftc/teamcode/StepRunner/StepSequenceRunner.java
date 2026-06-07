package org.firstinspires.ftc.teamcode.StepRunner;

public class StepSequenceRunner {
    private final Step[] steps;
    private int activeStepIndex = 0;
    private boolean activeStepInitialized = false;

    public StepSequenceRunner(Step[] steps) {
        this.steps = steps;
    }

    public void reset() {
        activeStepIndex = 0;
        activeStepInitialized = false;
    }

    public void update() {
        if (activeStepIndex >= steps.length) {
            return;
        }

        Step activeStep = steps[activeStepIndex];

        if (!activeStepInitialized) {
            activeStep.init();
            activeStepInitialized = true;
        }

        activeStep.update();

        if (activeStep.isFinished()) {
            activeStep.exit();

            activeStepIndex++;
            activeStepInitialized = false;
        }
    }

    public boolean isFinished() {
        return (activeStepIndex >= steps.length);
    }

    public void forceExit() {
        if (activeStepIndex >= steps.length) {
            return;
        }

        Step activeStep = steps[activeStepIndex];
        activeStep.exit();
    }
}
