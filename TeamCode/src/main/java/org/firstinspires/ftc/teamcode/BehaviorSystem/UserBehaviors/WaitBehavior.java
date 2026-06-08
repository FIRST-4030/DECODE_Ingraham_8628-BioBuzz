package org.firstinspires.ftc.teamcode.BehaviorSystem.UserBehaviors;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.BehaviorSystem.Behavior;

public class WaitBehavior extends Behavior {
    private final ElapsedTime elapsedTime = new ElapsedTime();
    private final double waitTimeMS;

    public WaitBehavior(double waitTimeMS) {
        this.waitTimeMS = waitTimeMS;
    }

    @Override
    public void init() {
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
}
