package edu.fpms.faltech.robot;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by ddhol on 12/1/2015.
 */


public class Hopper {

    private LinearOpMode opMode;

    private Servo hopperServo;


    public Hopper(LinearOpMode opMode) throws InterruptedException {
        this.opMode = opMode;
        opMode.telemetry.addData("mtthd: ", "DriveTrain constructor");
        // get hardware mappings
        hopperServo = opMode.hardwareMap.servo.get("HopperSrv");
        hopperServo.setPosition(.5);
    }

    //goLeft
    public void goLeft(int milliseconds) throws InterruptedException {
        hopperServo.setPosition(0);
        wait(milliseconds);
        stop();
    }

    //goRight
    public void goRight(int milliseconds) throws InterruptedException {
        hopperServo.setPosition(1);
        wait(milliseconds);
        stop();
    }

    //stop
    public void stop() {
        hopperServo.setPosition(.5);
    }
}
