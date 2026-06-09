package org.firstinspires.ftc.teamcode;

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
}
