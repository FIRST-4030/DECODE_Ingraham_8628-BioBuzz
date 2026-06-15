package org.firstinspires.ftc.teamcode;

import android.os.Environment;

import com.pedropathing.follower.Follower;
import com.pedropathing.ftc.FollowerBuilder;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Pedro.PedroConstants;
import org.firstinspires.ftc.teamcode.Pedro.PedroConstantsBioBuzzCompetitionBot;
import org.firstinspires.ftc.teamcode.Pedro.PedroConstantsDecodeBot;
import org.firstinspires.ftc.teamcode.Pedro.PedroConstantsBioBuzzDemoBot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ControlHub {
    public enum BotIdentification {
        DECODE,
        BIOBUZZ_COMPETITION,
        BIOBUZZ_DEMO,
        UNKNOWN,
    }

    private final String fileName;

    public ControlHub() {
        String logFolder = Environment.getExternalStorageDirectory().getPath(); // /storage/emulated/0 also maps to /sdcard
        fileName = logFolder + "/FIRST/Datalogs/ControlHub.txt";
    }

    public boolean createControlHubFile() throws IOException {
        File file = new File(fileName);

        file.createNewFile();
        return file.isFile();
    }

    public boolean deleteControlHubFile() {
        File file = new File(fileName);

        return file.delete();
    }

    public String getControlHubFileName() {
        return fileName;
    }

    public void initializeControlHub(BotIdentification botIdentification) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(botIdentificationToString(botIdentification));
        }
    }

    public BotIdentification getBotIdentification() throws FileNotFoundException {
        String line = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            line = reader.readLine();
        } catch (IOException e) {
            // Catches potential FileNotFoundException (which is an IOException)
            // and other I/O errors
            System.err.println("An error occurred while reading the file: " + e.getMessage());
        }

        return stringToBotIdentification(line);
    }

    public String botIdentificationToString(BotIdentification botIdentification) {
        switch (botIdentification) {
            case DECODE:
                return "DECODE";
            case BIOBUZZ_COMPETITION:
                return "BIOBUZZ_COMPETITION";
            case BIOBUZZ_DEMO:
                return "BIOBUZZ_DEMO";
            default:
                return "UNKNOWN";
        }
    }

    public BotIdentification stringToBotIdentification(String string) {
        switch (string) {
            case "DECODE":
                return BotIdentification.DECODE;
            case "BIOBUZZ_COMPETITION":
                return BotIdentification.BIOBUZZ_COMPETITION;
            case "BIOBUZZ_DEMO":
                return BotIdentification.BIOBUZZ_DEMO;
            default:
                return BotIdentification.UNKNOWN;
        }
    }

    public PedroConstants getRobotSpecificPedroConstants() {
        try {
            switch (getBotIdentification()) {
                case DECODE:
                    return new PedroConstantsDecodeBot();
                case BIOBUZZ_COMPETITION:
                    return new PedroConstantsBioBuzzCompetitionBot();
                case BIOBUZZ_DEMO:
                    return new PedroConstantsBioBuzzDemoBot();
                default:
                    return new PedroConstantsBioBuzzCompetitionBot(); // Return competition constants
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Follower createFollower(HardwareMap hardwareMap) {
        PedroConstants pedroConstants = getRobotSpecificPedroConstants();

        return new FollowerBuilder(getRobotSpecificPedroConstants().getFollowerConstants(), hardwareMap)
                .pathConstraints(pedroConstants.getPathConstraints())
                .mecanumDrivetrain(pedroConstants.getDriveConstraints())
                .pinpointLocalizer(pedroConstants.getLocalizerConstants())
                .build();
    }
}
