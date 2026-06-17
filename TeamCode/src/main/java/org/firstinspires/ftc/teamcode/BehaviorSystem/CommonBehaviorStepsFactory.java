package org.firstinspires.ftc.teamcode.BehaviorSystem;

import com.pedropathing.follower.Follower;
import com.pedropathing.paths.PathChain;

import org.firstinspires.ftc.teamcode.BehaviorSystem.Behaviors.FollowPathBehavior;
import org.firstinspires.ftc.teamcode.BehaviorSystem.Behaviors.WaitBehavior;

public class CommonBehaviorStepsFactory {
    public static BehaviorStep makeFollowPathBehaviorStep(Follower follower, PathChain pathChain) {
        return new BehaviorStep(
                new FollowPathBehavior(follower, pathChain)
        );
    }

    public static BehaviorStep makeWaitBehaviorStep(double waitTimeMS) {
        return new BehaviorStep(
                new WaitBehavior(waitTimeMS)
        );
    }
}
