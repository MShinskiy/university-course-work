import lejos.nxt.*;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.*;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ass2 {
    public static void main(String[] args) {
        //Motors
        final NXTRegulatedMotor LEFT = Motor.C;
        final NXTRegulatedMotor RIGHT = Motor.A;

        boolean finish = false;

        //Define starting & goal points
        Waypoint[] points = new Waypoint[]{new Waypoint(0, 0), new Waypoint(150, 150)};
        Waypoint currPoint;

        //Attach sensors to the ports
        ColorSensor colorSensor = new ColorSensor(SensorPort.S4);
        UltrasonicSensor sonicSensor = new UltrasonicSensor(SensorPort.S1);

        //File & Streams
        File file = new File("CE215ass2data.csv");
        FileOutputStream fileStream = null;
        DataOutputStream dataStream = null;

        //Creation and setup of file and streams
        try {
            file.createNewFile();
            fileStream = new FileOutputStream(file);
            dataStream = new DataOutputStream(fileStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Pilot
        DifferentialPilot pilot = new DifferentialPilot(3.37, 17.25, LEFT, RIGHT);
        OdometryPoseProvider opp = new OdometryPoseProvider(pilot);
        pilot.addMoveListener(opp);

        //Navigation
        Navigator nav = new Navigator(pilot);
        nav.setPoseProvider(opp);
        nav.singleStep(true);

        opp.setPose(new Pose(0, 0, 90));
        currPoint = points[0];

        while (!finish) {
            nav.goTo(currPoint);
            while (nav.isMoving()) {
                if (sonicSensor.getDistance() < 15) {
                    nav.stop();
                    pilot.rotate(-90);
                    pilot.arc(20, 180);
                    nav.goTo(currPoint);
                }
                //If the sensor detects red colour at the waypoint
                if (colorSensor.getColorID() == 0 && nav.pathCompleted()) {
                    if(currPoint == points[0]) currPoint = points[1];
                    else finish = true;
                    nav.stop();// move to next waypoint
                    //If the waypoint has been reached but not at the red spot
                } else if (nav.pathCompleted() && colorSensor.getColorID() != 0) {
                    //Beginning of exploring algorithm
                    boolean found = false;
                    int s = 20;   //distance, constant magnitude
                    int a = 60;     //angle of rotation, constant
                    int count = 0;  //constant, to keep track of number of cycles complete
                    while (!found) {
                        //if the number of COUNT is even then increase the angle
                        if (count % 2 == 0 && count != 0) pilot.rotate(a);
                        pilot.travel(s);
                        s *= -1; //reverse the direction of travel
                        while (pilot.isMoving()) {
                            if (colorSensor.getColorID() == 0) {
                                found = true;
                                if(currPoint == points[0]) currPoint = points[1];
                                else finish = true;
                                nav.stop();
                            }
                        }
                        count++;
                    }
                }
                try {
                    LCD.setPixel(1 + (int) opp.getPose().getX() / 3, 1 + (int) opp.getPose().getY() / 3, 1);
                    dataStream.writeChars(opp.getPose().getX() + ", ");
                    dataStream.writeChars(opp.getPose().getY() + "\n ");
                    Thread.sleep(50);
                } catch (IOException | NullPointerException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            fileStream.close();
            dataStream.close();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
        Button.waitForAnyPress();
    }
}
