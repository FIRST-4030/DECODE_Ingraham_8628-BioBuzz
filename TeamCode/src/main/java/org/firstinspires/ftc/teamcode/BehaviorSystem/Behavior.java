package org.firstinspires.ftc.teamcode.BehaviorSystem;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public  interface Behavior {
    void enter();
    void update();
    boolean isFinished();
    void exit();
    void processTelemetry(Telemetry telemetry, String prefix);
}
