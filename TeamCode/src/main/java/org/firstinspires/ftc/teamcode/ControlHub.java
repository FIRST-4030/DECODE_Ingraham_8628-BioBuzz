package org.firstinspires.ftc.teamcode;

import android.os.Environment;

import com.pedropathing.follower.Follower;
import com.pedropathing.ftc.FollowerBuilder;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Pedro.PedroConstants;
import org.firstinspires.ftc.teamcode.Pedro.PedroConstantsCompetition;
import org.firstinspires.ftc.teamcode.Pedro.PedroConstantsDemo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ControlHub {

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

    public void initializeControlHub(String type) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(type);
        }
    }

    public String getControlHub() throws FileNotFoundException {
        String line = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            line = reader.readLine();
        } catch (IOException e) {
            // Catches potential FileNotFoundException (which is an IOException)
            // and other I/O errors
            System.err.println("An error occurred while reading the file: " + e.getMessage());
        }
        return line;
    }

    public PedroConstants getRobotSpecificPedroConstants() {
        Boolean robotIsDemo = false; // TODO: Properly check which robot it is, based on storing a file on the robot's sd card
        if (robotIsDemo) {
            return new PedroConstantsDemo();
        }
        return new PedroConstantsCompetition();
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