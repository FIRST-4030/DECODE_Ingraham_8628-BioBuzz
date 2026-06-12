package org.firstinspires.ftc.teamcode.BehaviorSystem.UserBehaviors;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.BehaviorSystem.Behavior;

/**
 * Behavior that completes after a given duration. Does not take any action during update().
 */
public class WaitBehavior implements Behavior {
    private final ElapsedTime elapsedTime = new ElapsedTime();
    private final double waitTimeMS;

    public WaitBehavior(double waitTimeMS) {
        this.waitTimeMS = waitTimeMS;
    }

    /**
     * Returns the current time remaining in milliseconds.
     * @return the current time remaining in milliseconds.
     */
    double getTimeRemainingMS() {
        return waitTimeMS - elapsedTime.milliseconds();
    }

    /**
     * Resets the timer.
     */
    @Override
    public void enter() {
        elapsedTime.reset();
    }

    /**
     * Does nothing.
     */
    @Override
    public void update() {}

    /**
     * Returns whether the given duration has elapsed.
     * @return whether the given duration has elapsed.
     */
    @Override
    public boolean isComplete() {
        return (elapsedTime.milliseconds() >= waitTimeMS);
    }

    /**
     * Does nothing.
     */
    @Override
    public void exit() {}

    @Override
    public void processTelemetry(Telemetry telemetry, String prefix) {
        telemetry.addLine(prefix + "(MS remaining: " + Math.floor(getTimeRemainingMS()) + " MS)");
    }
}
