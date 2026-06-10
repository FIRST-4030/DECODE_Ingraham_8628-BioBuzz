package org.firstinspires.ftc.teamcode.BehaviorSystem;

import java.util.function.Supplier;

public interface State {
    void enter();

    void update();

    State getNextState();

    void exit();
}
