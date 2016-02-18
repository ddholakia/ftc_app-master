package edu.fpms.faltech.robot;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by ddhol on 12/1/2015.
 */


public class Climber_Savers {

    private LinearOpMode opMode;

    private Servo climberSaverServo;

    private double STARTPOSITION = 1;
    private double RELEASEPOSITION = 0;
    private double TELEOPPOSITION = .5;

    public Climber_Savers(LinearOpMode opMode) throws InterruptedException {
        this.opMode = opMode;
        opMode.telemetry.addData("ClimberSavers", "constructor");
        // get hardware mappings
        climberSaverServo = opMode.hardwareMap.servo.get("ClimberSaverServo");
        climberSaverServo.setPosition(STARTPOSITION);
    }

    public void ClimberStartPosition() {
        climberSaverServo.setPosition(STARTPOSITION);
    }

    public void ClimberReleasePosition() {
        climberSaverServo.setPosition(RELEASEPOSITION);
    }

    public void ClimberTeleopPosition() {
        climberSaverServo.setPosition(TELEOPPOSITION);
    }
}
