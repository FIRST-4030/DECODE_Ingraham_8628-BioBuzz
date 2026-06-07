package org.firstinspires.ftc.teamcode.StepRunner;

import com.qualcomm.robotcore.util.ElapsedTime;

public class WaitStep implements Step {
    private final ElapsedTime elapsedTime = new ElapsedTime();
    private final double waitTimeMS;

    public WaitStep(double waitTimeMS) {
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
