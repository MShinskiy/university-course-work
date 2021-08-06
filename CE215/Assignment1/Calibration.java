import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.robotics.navigation.DifferentialPilot;

import java.lang.*;

public class Calibration {
    /**
     * Calibration of Diameter:
     *
     * Take a measuring tape
     * Align the axis of the robot wheel with 0 mark on the measuring tape
     * Spin servos on robot for 360 degrees
     * Measure the distance that robot travelled
     * Check if it went for 360 degrees with getTachoCount()
     * Divide by Pi
     * Get D
     *
     * Calibration of Track Width:
     *
     * Turn robot for certain angle
     * Read the angle for which it turned and change to radians
     * Read the encoder counts for each motor
     * Put both numbers into formula
     * Get W
     */

    final NXTRegulatedMotor right = Motor.A;
    final NXTRegulatedMotor left = Motor.C;

    Calibration() {
        //diameter();
        track();
    }

    //Diameter Calibration
    void diameter() {
        //Clear LCD
        LCD.clear();

        //reset count
        right.resetTachoCount();
        left.resetTachoCount();

        left.rotate(360, true);
        right.rotate(360);

        left.stop(true);
        right.stop(true);

        //Display counts for each servo motor
        LCD.drawString("left" + left.getTachoCount(), 0, 0);
        LCD.drawString("right" + right.getTachoCount(), 0, 1);
        Button.waitForAnyPress();
    }

    void track(){
        //Clear LCD
        LCD.clear();

        //reset count
        right.resetTachoCount();
        left.resetTachoCount();

        //Create differential pilot with known diameter of a wheel and approximated track width
        DifferentialPilot dp = new DifferentialPilot(3.37, 17.25, left, right);

        //Turn 90
        dp.rotate(180);

        //Display counts for each servo motor
        LCD.drawString("left" + left.getTachoCount(), 0, 0);
        LCD.drawString("right" + right.getTachoCount(), 0, 1);
        Button.waitForAnyPress();
    }

    public static void main(String[] args){
        new Calibration();
    }
}