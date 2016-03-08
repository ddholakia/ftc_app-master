/*

FALTECH 7079
FOUR POINTS MIDDLE SCHOOL

AUTONOMOUS CLASS NAMING NOMENCLATURE:
    OpMode Type: {Auto|Telop}
    Alliance color: {R|B} // Red or Blue
    Starting Position: {CS|M|MS} // Corner Side or Middle or Mountain Side
    Scoring strategy: {guys|none|beacon}
    End location: {PZ|SZ} // Parking Zone or Safety Zone

DESCRIPTION:
    Autonomous program for Blue alliance, starting in Corner Side, scoring the climbers in the box,
    and finishing in the Parking Zone.
TARGET SCORE:
45

 */

package edu.fpms.faltech;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import edu.fpms.faltech.robot.Robot;

/**
 * A simple example of a linear op mode that will approach an IR beacon
 */
public class Red_M_ extends LinearOpMode {

    private Robot robot;

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new Robot(this);

        robot.arms.hopper.stop();
        robot.driveTrain.churro_grabber.Up();
        robot.climberSavers.ClimberStartPosition();
        // wait for the start button to be pressed
        waitForStart();
/* This is a Autonomous for red alliance,
It starts at the middle and ends at the beacon.
 */
        //Auto Start
        robot.collector.Flush();
        robot.driveTrain.GoStraitInches(-50, .5, 60);
        robot.driveTrain.PivotTurn(-33, .5, 30);
        robot.driveTrain.GoStraitInches(-55, .5, 30);
        robot.driveTrain.PivotTurn(-33, .5, 30);
        robot.collector.Stop();
        robot.collector.Collect();
        robot.driveTrain.GoStraitInches(-25, .5, 30);
        robot.collector.Stop();

        /*
        if (robot.driveTrain.ApproachBeacon(4)){
            // do something like drop off guys

        }
        */
    }
}
