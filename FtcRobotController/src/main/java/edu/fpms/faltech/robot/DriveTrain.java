package edu.fpms.faltech.robot;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by ddhol on 12/1/2015.
 */


public class DriveTrain {

    final static int pulsesPerRevolution = 1680;
    final static double tireCircumference = 12.56; //inches

    private LinearOpMode opMode;
    private DcMotor leftMotor;
    private DcMotor rightMotor;

    private GyroSensor gyroSensor;
    // private int heading;
    public Churro_Grabber churro_grabber;


    public DriveTrain(LinearOpMode opMode) throws InterruptedException {
        this.opMode = opMode;
        opMode.telemetry.addData("DriveTrain", "constructor");
        // get hardware mappings
        leftMotor = opMode.hardwareMap.dcMotor.get("leftMotor");
        rightMotor = opMode.hardwareMap.dcMotor.get("rightMotor");
        gyroSensor = opMode.hardwareMap.gyroSensor.get("gyroSensor");

        churro_grabber = new Churro_Grabber(opMode);

        leftMotor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        rightMotor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

        rightMotor.setDirection(DcMotor.Direction.REVERSE);

        // calibrate the gyro.
        gyroSensor.calibrate();
        // make sure the gyro is calibrated.
        while (gyroSensor.isCalibrating()) {
            Thread.sleep(50);
        }
        gyroSensor.resetZAxisIntegrator();
        opMode.waitForNextHardwareCycle();
    }

    //stopMotors
    private void stopMotors() throws InterruptedException {

        opMode.telemetry.addData("StopMotors", "");

        leftMotor.setPower(0);
        rightMotor.setPower(0);
        opMode.waitForNextHardwareCycle();
    }

    // getEncoderAverage
    private long getEncoderAverage() {
        return (leftMotor.getCurrentPosition() + rightMotor.getCurrentPosition()) / 2;
    }

    private boolean Go(long distance, double power, int timeout) throws InterruptedException {
        ElapsedTime timer = new ElapsedTime();

        // resetEncoders();

        leftMotor.setPower(power);
        rightMotor.setPower(power);

        //long currentPosition = getEncoderAverage();
        long currentPosition = Math.abs(rightMotor.getCurrentPosition());

        if (power > 0) { //go forward
            long targetPosition = Math.abs(currentPosition) + distance;
            while ((currentPosition < targetPosition) && (timer.time() < timeout)) {
                currentPosition = Math.abs(rightMotor.getCurrentPosition());
                //currentPosition = getEncoderAverage();
                opMode.telemetry.addData("Pos: ", currentPosition);
                opMode.telemetry.addData("TPos: ", targetPosition);
            }
        } else if (power < 0) { //go backward
            long targetPosition = Math.abs(currentPosition - distance);
            while ((currentPosition < targetPosition) && (timer.time() < timeout)) {
                currentPosition = Math.abs(rightMotor.getCurrentPosition());
                //currentPosition = getEncoderAverage();
                opMode.telemetry.addData("Pos: ", currentPosition);
                opMode.telemetry.addData("TPos: ", targetPosition);
            }
        }

        stopMotors();

        return timer.time() <= timeout;
    }

    //PivotRight
    private void PivotRight(double power) {
        opMode.telemetry.addData("PivotRight Power", power);
        leftMotor.setPower(power);
        rightMotor.setPower(-power);
    }

    //PivotLeft
    private void PivotLeft(double power) {
        opMode.telemetry.addData("PivotLeft Power", power);
        leftMotor.setPower(-power);
        rightMotor.setPower(power);
    }

    //GoInches
    public boolean GoInches(double inches, double power, int seconds) throws InterruptedException {
        opMode.telemetry.addData("GoInches", inches);
        long distance = (long) (((Math.abs(inches) / tireCircumference) * pulsesPerRevolution));
        return Go(distance, power, seconds);
    }

    //Pivot Turn
    public boolean PivotTurn(int degrees, double power, int seconds) throws InterruptedException {
        opMode.telemetry.addData("PivotTurn", degrees);
        ElapsedTime timer = new ElapsedTime();

        if ((degrees > 359) || (degrees < -359)) {
            opMode.telemetry.addData("Error", "Incorrect Degree Value" + degrees);
            return false;
        }

        Thread.sleep(500);
        gyroSensor.resetZAxisIntegrator();// set heading to zero
        while (gyroSensor.isCalibrating()) {
            Thread.sleep(50);
        }
        opMode.waitForNextHardwareCycle();

        if (degrees > 0) { // right turn
            opMode.telemetry.addData("Right Turn", degrees);
            PivotRight(power);
            int heading = 0;
            while (heading < degrees) {
                if (timer.time() > seconds) {
                    stopMotors();
                    return false;
                }
                heading = gyroSensor.getHeading();
                opMode.telemetry.addData("Heading", heading);

            }
            stopMotors();
        } else if (degrees < 0) { // left turn
            opMode.telemetry.addData("Left Turn", degrees);
            PivotLeft(power);
            int heading = 359;
            while ((heading > (360 + degrees)) || (heading == 0)) {
                if (timer.time() > seconds) {
                    stopMotors();
                    return false;
                }

                heading = gyroSensor.getHeading();
                opMode.telemetry.addData("Heading", heading);
            }
            stopMotors();
        }
        return true;
    }

    //Gyro Test
    public void GyroTest() {
        while (true) {
            opMode.telemetry.addData("Heading", gyroSensor.getHeading());
        }
    }
}
