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
public class Auto_B_M_guys_SZ extends LinearOpMode {

    private Robot robot;


    @Override
    public void runOpMode() throws InterruptedException {
        robot = new Robot(this);

        robot.arms.hopper.stop();
        robot.driveTrain.churro_grabber.Up();
        robot.climberSavers.ClimberStartPosition();
        // wait for the start button to be pressed
        waitForStart();
/* This is a Autonomous for blue alliance,
It goes from the corner to drop off the climbers
into the shelter. Then it ends in the Floor Goal.
 */
        //Auto Start
        robot.collector.Flush();
        robot.driveTrain.GoInches(87, -.5, 20);
        robot.driveTrain.PivotTurn(30, .5, 5);
        robot.climberSavers.ClimberReleasePosition();
        robot.collector.Stop();
        sleep(5000);
        robot.climberSavers.ClimberTeleopPosition();
        //robot.driveTrain.PivotTurn(90, .5, 10);
        //robot.driveTrain.GoInches(75, -.5, 5);
        //robot.driveTrain.PivotTurn(-90,.5,10);


    }
}
