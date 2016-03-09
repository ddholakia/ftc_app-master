package edu.fpms.faltech.robot;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by ddhol on 12/1/2015.
 */


public class Robot {

    private LinearOpMode opMode;

    public DriveTrain driveTrain;
    public Arms arms;
    public Collector collector;
    public Climber_Savers climberSavers;
    public Beacon beacon;

    public Robot(LinearOpMode opMode) throws InterruptedException {
        this.opMode = opMode;
        opMode.telemetry.addData("Robot", "constructor");
        // get hardware mappings

        driveTrain = new DriveTrain(opMode);
        arms = new Arms(opMode);
        collector = new Collector(opMode);
        climberSavers = new Climber_Savers(opMode);
        // beacon = new Beacon2(opMode, false);

        driveTrain.churro_grabber.Up();
        climberSavers.ClimberStartPosition();
    }

}
