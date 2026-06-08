package org.firstinspires.ftc.teamcode.BehaviorSystem;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class StateMachine {
    private State activeState = null;

    private final Telemetry telemetry;

    public StateMachine(Telemetry telemetry) {
        this.telemetry = telemetry;
    }

    public void init(State initialState) {
        activeState = initialState;
        activeState.enter();
    }

    public void update() {
        if (activeState == null) {
            return;
        }

        activeState.update();

        State nextStateOrNull = activeState.getNextStateOrNull();

        if (nextStateOrNull != null) {
            activeState.exit();
            activeState = nextStateOrNull;
            activeState.enter();
        }
    }

    public void changeState(State state) {
        activeState.exit();
        activeState = state;
        activeState.enter();
    }
}
