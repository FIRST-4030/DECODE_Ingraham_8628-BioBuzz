package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.BehaviorSystem.BehaviorStep;
import org.firstinspires.ftc.teamcode.BehaviorSystem.UserBehaviors.FollowPathBehavior;
import org.firstinspires.ftc.teamcode.BehaviorSystem.BehaviorStepSequence;

@Autonomous(name="Step Sequence Runner Demo", group="Linear OpMode")
public class StepSequenceRunnerDemo extends LinearOpMode {
    BehaviorStep moveToOneSpot = new BehaviorStep(
            new FollowPathBehavior(null, null)
    );

    BehaviorStep moveToAnotherSpot = new BehaviorStep(
            new FollowPathBehavior(null, null)
    );


    BehaviorStepSequence farAuto = new BehaviorStepSequence(
            new BehaviorStep[]{
                    moveToOneSpot,
                    moveToAnotherSpot
            }
    );

    @Override
    public void runOpMode() {
        // ------ INIT ------
        do {

        } while (opModeInInit());

        // ------ PLAY ------
        do {

        } while (opModeIsActive());
    }

    public void drive() {

    }
}
