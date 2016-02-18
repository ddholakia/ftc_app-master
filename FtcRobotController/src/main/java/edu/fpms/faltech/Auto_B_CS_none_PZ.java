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
    Autonomous program for Blue alliance, starting in Corner Side and finishing in the Parking Zone.
TARGET SCORE:
    5

 */

package edu.fpms.faltech;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import edu.fpms.faltech.robot.Robot;

/* This is a Autonomous for blue alliance,
It goes from the corner to the Floor Goal.
 */
public class Auto_B_CS_none_PZ extends LinearOpMode {

    private Robot robot;

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new Robot(this);
        // wait for the start button to be pressed
        waitForStart();

        //Auto Start
        robot.driveTrain.GoInches(123, .5, 15);
        robot.driveTrain.PivotTurn(45, .5, 3);
        robot.driveTrain.GoInches(35, .5, 5);
        robot.driveTrain.PivotTurn(90, .5, 3);
        robot.driveTrain.GoInches(34, .5, 5);
    }
}
