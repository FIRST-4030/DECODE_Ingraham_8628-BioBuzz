package org.firstinspires.ftc.teamcode.BehaviorSystem;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class StateMachine {
    private State activeState = null;

    public StateMachine() {
    }

    public void setInitialState(State initialState) {
        activeState = initialState;
        activeState.enter();
    }

    public void update() {
        if (activeState == null) {
            return;
        }

        activeState.update();

        State nextState = activeState.getNextState();

        if (nextState != activeState) {
            activeState.exit();
            activeState = nextState;
            activeState.enter();
        }
    }

    public void changeState(State state) {
        activeState.exit();
        activeState = state;
        activeState.enter();
    }

    public void processTelemetry(Telemetry telemetry) {
        telemetry.addLine("--- STATE MACHINE ---");
        telemetry.addData("Active state", activeState.getClass().getSimpleName());
        telemetry.addLine();
    }
}
