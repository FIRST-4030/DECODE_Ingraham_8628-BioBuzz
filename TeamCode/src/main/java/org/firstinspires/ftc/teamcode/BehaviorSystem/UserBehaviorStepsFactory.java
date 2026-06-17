package org.firstinspires.ftc.teamcode.BehaviorSystem;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.BehaviorSystem.Behaviors.RealTimeBehavior;
import org.firstinspires.ftc.teamcode.BehaviorSystem.Behaviors.WaitBehavior;
import org.firstinspires.ftc.teamcode.Chassis;

public class UserBehaviorStepsFactory {
    public static BehaviorStep makeTimedRealTimeStep(Chassis chassis, Gamepad gamepad, double waitTimeMS) {
        return new BehaviorStep(
                BehaviorStep.StepCompletedConditionType.ON_PRIMARY_BEHAVIOR_COMPLETED,
                new WaitBehavior(waitTimeMS),
                new Behavior[] {
                        new RealTimeBehavior(chassis, gamepad)
                }
        );
    }
}
