package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.StepRunner.Behavior;
import org.firstinspires.ftc.teamcode.StepRunner.FollowPathBehavior;
import org.firstinspires.ftc.teamcode.StepRunner.Step;
import org.firstinspires.ftc.teamcode.StepRunner.StepSequenceRunner;
import org.firstinspires.ftc.teamcode.StepRunner.WaitBehavior;
import org.firstinspires.ftc.teamcode.StepRunner.WaitForConditionBehavior;

@Autonomous(name="Step Runner Demo Auto", group="Linear OpMode")
public class StepSequenceRunnerDemo extends LinearOpMode {
    Step moveToShootAndWait = new Step(
            Step.StepType.FINISHED_ON_ALL,
            new FollowPathBehavior(null, null),
            new Behavior[] {
                    new WaitBehavior(5000)
            }
    );

    public boolean condition() {
        return true;
    }

    Step waitForCondition = new Step(
            Step.StepType.FINISHED_ON_PRIMARY,
            new WaitForConditionBehavior(this::condition)
    );

    StepSequenceRunner demoStepChain = new StepSequenceRunner(
            new Step[] {
                    moveToShootAndWait,
                    waitForCondition,
                    moveToShootAndWait,
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
