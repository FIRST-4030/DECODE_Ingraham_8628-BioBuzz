package org.firstinspires.ftc.teamcode.BehaviorSystem.User;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.BehaviorSystem.Behavior;

/**
 * Behavior that completes after a given duration. Does not take any action during update().
 */
public class WaitBehavior implements Behavior {
    private final ElapsedTime elapsedTime = new ElapsedTime();
    private final double waitTimeMS;
    private boolean entered = false;
    private final String label;

    public WaitBehavior(double waitTimeMS) {
        this(waitTimeMS, "WaitBehavior");
    }

    public WaitBehavior(double waitTimeMS, String label) {
        this.waitTimeMS = waitTimeMS;
        this.label = label;
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
        entered = true;
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
        return (elapsedTime.milliseconds() >= waitTimeMS && entered);
    }

    /**
     * Does nothing.
     */
    @Override
    public void exit() {
        entered = false;
    }

    @Override
    public void processTelemetry(Telemetry telemetry, String prefix) {
        if (entered) {
            telemetry.addLine(prefix + "(MS remaining: " + Math.floor(getTimeRemainingMS()) + " MS)");
        } else {
            telemetry.addLine(prefix + "Has not begun waiting yet");
        }
    }

    @Override
    public String getLabel() {
        return label;
    }
}
