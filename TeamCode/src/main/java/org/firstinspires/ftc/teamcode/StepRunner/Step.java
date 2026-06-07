package org.firstinspires.ftc.teamcode.StepRunner;

public interface Step {
    void init();
    void update();
    boolean isFinished();
    void exit();
}
