package org.firstinspires.ftc.teamcode.BehaviorSystem.UserBehaviors;
import org.firstinspires.ftc.teamcode.BehaviorSystem.Behavior;

import java.util.function.Supplier;

public class WaitForConditionBehavior extends Behavior {
    private final Supplier<Boolean> condition;

    public WaitForConditionBehavior(Supplier<Boolean> condition) {
        this.condition = condition;
    }

    @Override
    public void init() {}

    @Override
    public void update() {}


    @Override
    public boolean isFinished() {
        return condition.get();
    }

    @Override
    public void exit() {};
}
