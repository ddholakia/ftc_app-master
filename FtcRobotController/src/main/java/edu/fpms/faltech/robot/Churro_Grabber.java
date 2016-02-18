package edu.fpms.faltech.robot;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by ddhol on 12/1/2015.
 */


public class Churro_Grabber {

    private LinearOpMode opMode;

    private Servo ChurroGrab1;
    private Servo ChurroGrab2;


    public Churro_Grabber(LinearOpMode opMode) throws InterruptedException {
        this.opMode = opMode;
        opMode.telemetry.addData("mtthd: ", "Churro_Grabber constructor");
        // get hardware mappings
        ChurroGrab1 = opMode.hardwareMap.servo.get("ChurroGrab1");
        ChurroGrab2 = opMode.hardwareMap.servo.get("ChurroGrab2");
    }

    //set the Churro Grabber's position
    public void setChurroGrabers(int position) {
        ChurroGrab1.setPosition(position);
        ChurroGrab1.setPosition(position);
    }
}
