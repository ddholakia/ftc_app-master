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
     and finishing in the Safety Zone.
TARGET SCORE:
    45

 */

package edu.fpms.faltech;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import edu.fpms.faltech.robot.Robot;

/* This is a Autonomous for blue alliance,
It goes from the corner to drop off the climbers
into the shelter. Then it ends in the Blue Allience Repair Zone.
 */
public class Auto_B_CS_guys_SZ extends LinearOpMode {

    private Robot robot;

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new Robot(this);
        // wait for the start button to be pressed
        waitForStart();

        //Auto Start
        robot.driveTrain.GoInches(174, .5, 15);
        robot.driveTrain.PivotTurn(-30, .5, 3);
        robot.driveTrain.GoInches(44, -.5, 5);
        robot.arms.elevator.UpDegrees(30);
        robot.arms.hopper.goRight(4000);
        robot.arms.hopper.goLeft(4000);
        robot.arms.elevator.UpDegrees(0);

    }
}
