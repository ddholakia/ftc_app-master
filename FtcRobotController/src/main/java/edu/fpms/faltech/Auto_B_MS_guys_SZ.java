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
     Autonomous program for Blue alliance, starting in Mountain Side, scoring the climbers in the box,
     and finishing in the Safety Zone.
TARGET SCORE:
    45

 */

package edu.fpms.faltech;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import edu.fpms.faltech.robot.Robot;

/**
 * A simple example of a linear op mode that will approach an IR beacon
 */
public class Auto_B_MS_guys_SZ extends LinearOpMode {

    private Robot robot;

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new Robot(this);
        // wait for the start button to be pressed
        waitForStart();

        //Auto Start
        robot.driveTrain.GoInches(54, .5, 15);
        robot.driveTrain.PivotTurn(45, .5, 3);
        robot.driveTrain.GoInches(85, -.5, 5);
        robot.driveTrain.PivotTurn(30, .5, 3);
        robot.driveTrain.GoInches(41, .5, 6);
        robot.arms.elevator.UpDegrees(30);
        robot.arms.hopper.goRight(4000);
        robot.arms.hopper.goLeft(4000);
        robot.arms.elevator.UpDegrees(0);
    }
}
