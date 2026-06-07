package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.StepRunner.Step;
import org.firstinspires.ftc.teamcode.StepRunner.StepSequenceRunner;
import org.firstinspires.ftc.teamcode.StepRunner.WaitStep;

@Autonomous(name="Step Runner Demo Auto", group="Linear OpMode")
public class StepSequenceRunnerDemo extends LinearOpMode {
    StepSequenceRunner demoStepChain = new StepSequenceRunner(
            new Step[] {
                    new WaitStep(20)
            }
    );

    @Override
    public void runOpMode() throws InterruptedException {
        // ------ INIT ------
        do {

        } while (opModeInInit());

        // ------ PLAY ------
        do {
            demoStepChain.update();

        } while (opModeIsActive());
    }
}
