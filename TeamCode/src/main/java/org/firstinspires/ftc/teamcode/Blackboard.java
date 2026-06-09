package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Blackboard {
    public enum Alliance {
        RED,
        BLUE,
        UNKNOWN,
    }
    private static Alliance alliance = Alliance.UNKNOWN;

    public static Alliance getAlliance() {
        return alliance;
    };

    public static String getAllianceAsString() {
        switch (alliance) {
            case RED:
                return "Red";
            case BLUE:
                return "Blue";
            case UNKNOWN:
                return "Unknown";
            default:
                return "Null";
        }
    }

    public static void setAlliance(Alliance alliance) {
        Blackboard.alliance = alliance;
    }

    public static void initLoopProcess(Telemetry telemetry, Gamepad gamepad1) {
        telemetry.addLine("--- BLACKBOARD ---");
        telemetry.addData("Alliance", Blackboard.getAllianceAsString());
        telemetry.addLine("^^^ RB + A: Blue,  RB + B: Red ^^^");
        telemetry.addLine();

        if (gamepad1.right_bumper) {
            if (gamepad1.aWasPressed()) {
                Blackboard.setAlliance(Blackboard.Alliance.BLUE);
            } else if (gamepad1.bWasPressed()) {
                Blackboard.setAlliance(Blackboard.Alliance.RED);
            }
        }
    }
}
