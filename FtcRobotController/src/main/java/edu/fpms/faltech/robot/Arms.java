package edu.fpms.faltech.robot;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by ddhol on 12/1/2015.
 */


public class Arms {

    private LinearOpMode opMode;
    public Hopper hopper;
    public Elevator elevator;

    private DcMotor ArmRight;
    private DcMotor ArmLeft;

    private void MoveArms(double power) {
        ArmRight.setPower(power);
        ArmLeft.setPower(power);
    }

    public Arms(LinearOpMode opMode) throws InterruptedException {
        this.opMode = opMode;
        opMode.telemetry.addData("mtthd: ", "Arms constructor");
        // get hardware mappings
        ArmRight = opMode.hardwareMap.dcMotor.get("ArmRight");
        ArmLeft = opMode.hardwareMap.dcMotor.get("ArmLeft");
        elevator = new Elevator(opMode);
        hopper = new Hopper(opMode);
    }

    //Extend
    public void Extend(int seconds) throws InterruptedException {
        MoveArms(.5);
        wait(seconds * 1000);
        MoveArms(0);
    }

    //Retract
    public void Retract(int seconds) throws InterruptedException {
        MoveArms(-.25);
        wait(seconds * 1000);
        MoveArms(0);
    }

}

