package edu.fpms.faltech.robot;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by ddhol on 12/1/2015.
 */


public class Collector {

    private LinearOpMode opMode;

    private DcMotor Spinners;


    public Collector(LinearOpMode opMode) throws InterruptedException {
        this.opMode = opMode;
        opMode.telemetry.addData("mtthd: ", "Collector constructor");
        // get hardware mappings
        Spinners = opMode.hardwareMap.dcMotor.get("Spinners");

    }

    //Collect
    public void Collect() {
        Spinners.setPower(-1);
    }

    //Flush
    public void Flush() {
        Spinners.setPower(1);
    }

    //Stop
    public void Stop() {
        Spinners.setPower(0);
    }
}

