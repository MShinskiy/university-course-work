import lejos.nxt.*;
import lejos.robotics.Color;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Pose;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Assignment1 {
    public static void main(String[] args) {
        //Motors
        final NXTRegulatedMotor LEFT = Motor.C;
        final NXTRegulatedMotor RIGHT = Motor.A;
        final int trSpeed = 30;
        final int rtSpeed = 60;

        //File & Streams
        File file = new File("data.csv");
        FileOutputStream fileStream = null;
        DataOutputStream dataStream = null;

        //Creation and setup of file and streams
        try {
            file.createNewFile();
            fileStream = new FileOutputStream(file);
            dataStream = new DataOutputStream(fileStream);
            dataStream.writeChars("Travel speed: " + trSpeed + ",");
            dataStream.writeChars("Rotation speed: " + rtSpeed + "\n");
            dataStream.writeChars("x: " + ",");
            dataStream.writeChars("y: " + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Diff. pilot and odometry constructors
        DifferentialPilot pilot = new DifferentialPilot(3.37, 15.16, LEFT, RIGHT);
        OdometryPoseProvider opp = new OdometryPoseProvider(pilot);
        pilot.addMoveListener(opp);

        //Define speeds
        pilot.setTravelSpeed(trSpeed);
        pilot.setRotateSpeed(rtSpeed);

        //Reset position
        opp.setPose(new Pose(0, 0, 0));

        //Move in a square and get readings
        for (int x = 0; x < 4; x++) {
            pilot.travel(100, true);
            //Take readings when moving
            while (pilot.isMoving()) {
                try {
                    LCD.setPixel(5 + (int) opp.getPose().getX() / 2, 5 + (int) opp.getPose().getY() / 2, Color.BLACK);
                    dataStream.writeChars(opp.getPose().getX() + ", ");
                    dataStream.writeChars(opp.getPose().getY() + "\n ");
                    Thread.sleep(50);
                } catch (IOException | NullPointerException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (x < 3) pilot.rotate(90);
        }
        //close DataOutputStream and FileOutputStream
        try {
            fileStream.close();
            dataStream.close();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
        Button.waitForAnyPress();
    }
}
