package org.firstinspires.ftc.teamcode.StepRunner;

public class Step {
    public enum StepType {FINISHED_ON_PRIMARY, FINISHED_ON_ALL, FINISHED_ON_ANY}

    private final StepType stepType;
    private final Behavior primaryBehavior;
    private final Behavior[] secondaryBehaviors;

    public Step(StepType stepType, Behavior primaryBehavior) {
        this.stepType = stepType;
        this.primaryBehavior = primaryBehavior;
        this.secondaryBehaviors = new Behavior[0];
    }

    public Step(StepType stepType, Behavior primaryBehavior, Behavior[] secondaryBehaviors) {
        this.stepType = stepType;
        this.primaryBehavior = primaryBehavior;
        this.secondaryBehaviors = secondaryBehaviors;
    }

    public void init() {
        this.primaryBehavior.init();
        for (Behavior secondaryBehavior: this.secondaryBehaviors) {
            secondaryBehavior.init();
        }
    }

    public void update() {
        if (!this.primaryBehavior.isFinished()) {
            this.primaryBehavior.update();
        }
        for (Behavior secondaryBehavior: this.secondaryBehaviors) {

            if (!secondaryBehavior.isFinished()) {
                secondaryBehavior.update();
            }
        }
    }

    public boolean isFinished() {
        switch (this.stepType) {
            case FINISHED_ON_PRIMARY:
                return (this.primaryBehavior.isFinished());
            case FINISHED_ON_ALL:
                return allBehaviorsFinished();
            case FINISHED_ON_ANY:
                return anyBehaviorsFinished();
            default:
                return false;
        }
    }

    public boolean allBehaviorsFinished() {
        if (!this.primaryBehavior.isFinished()) {
            return false;
        }

        for (Behavior secondaryBehavior: this.secondaryBehaviors) {
            if (!secondaryBehavior.isFinished()) {
                return false;
            }
        }

        return true;
    }
    public boolean anyBehaviorsFinished() {
        if (this.primaryBehavior.isFinished()) {
            return true;
        }

        for (Behavior secondaryBehavior: this.secondaryBehaviors) {
            if (secondaryBehavior.isFinished()) {
                return true;
            }
        }

        return false;
    }

    public void exit() {
        this.primaryBehavior.exit();
        for (Behavior secondaryBehavior: this.secondaryBehaviors) {
            secondaryBehavior.exit();
        }
    }
}
