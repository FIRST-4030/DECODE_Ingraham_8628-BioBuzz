package org.firstinspires.ftc.teamcode.BehaviorSystem.UserBehaviors;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.BehaviorSystem.Behavior;

public class WaitBehavior implements Behavior {
    private final ElapsedTime elapsedTime = new ElapsedTime();
    private final double waitTimeMS;

    public WaitBehavior(double waitTimeMS) {
        this.waitTimeMS = waitTimeMS;
    }

    @Override
    public void enter() {
        elapsedTime.reset();
    }

    @Override
    public void update() {

    }

    @Override
    public boolean isFinished() {
        return (elapsedTime.milliseconds() >= waitTimeMS);
    }

    @Override
    public void exit() {

    }

    @Override
    public void processTelemetry(Telemetry telemetry, String prefix) {
        telemetry.addLine(prefix + "(MS remaining: " + Math.floor(waitTimeMS - elapsedTime.milliseconds()) + " MS)");
    }
}
