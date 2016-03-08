package edu.fpms.faltech.robot;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsAnalogOpticalDistanceSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by ddhol on 12/1/2015.
 */


public class DriveTrain {

    final static int pulsesPerRevolution = 1680;
    final static double tireCircumference = 12.56; //inches
    final static double gain = 7;

    private LinearOpMode opMode;
    private DcMotor leftMotor;
    private DcMotor rightMotor;


    private ModernRoboticsI2cGyro gyroSensor;
    private ModernRoboticsAnalogOpticalDistanceSensor distSensor;
    // private int heading;
    public Churro_Grabber churro_grabber;

    public DriveTrain(LinearOpMode opMode) throws InterruptedException {
        this.opMode = opMode;
        opMode.telemetry.addData("DriveTrain", "constructor");
        // get hardware mappings
        leftMotor = opMode.hardwareMap.dcMotor.get("leftMotor");
        rightMotor = opMode.hardwareMap.dcMotor.get("rightMotor");
        gyroSensor = (ModernRoboticsI2cGyro) opMode.hardwareMap.gyroSensor.get("gyroSensor");
        distSensor = (ModernRoboticsAnalogOpticalDistanceSensor) opMode.hardwareMap.opticalDistanceSensor.get("distSensor");

        churro_grabber = new Churro_Grabber(opMode);

        leftMotor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        rightMotor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

        //leftMotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        //rightMotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);

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

    private int getCorrectZHeading() {
        return gyroSensor.getIntegratedZValue() * -1;
    }

    //stopMotors
    private void stopMotors() throws InterruptedException {

        opMode.telemetry.addData("StopMotors", "");
        opMode.waitForNextHardwareCycle();
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
        long currentPosition = Math.abs(leftMotor.getCurrentPosition());

        if (power > 0) { //go forward
            long targetPosition = Math.abs(currentPosition) + distance;
            while ((currentPosition < targetPosition) && (timer.time() < timeout)) {
                currentPosition = Math.abs(leftMotor.getCurrentPosition());
                //currentPosition = getEncoderAverage();
                opMode.telemetry.addData("Pos: ", currentPosition);
                opMode.telemetry.addData("TPos: ", targetPosition);
            }
        } else if (power < 0) { //go backward
            long targetPosition = Math.abs(currentPosition - distance);
            while ((currentPosition < targetPosition) && (timer.time() < timeout)) {
                currentPosition = Math.abs(leftMotor.getCurrentPosition());
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

        power = Math.abs(power);

        if ((degrees > 180) || (degrees < -180)) {
            opMode.telemetry.addData("Error", "Incorrect Degree Value" + degrees);
            return false;
        }

        Thread.sleep(1000);
        gyroSensor.resetZAxisIntegrator();// set heading to zero
        opMode.waitForNextHardwareCycle();

        int heading = getCorrectZHeading();
        opMode.telemetry.addData("Heading", heading);
        int endHeading = heading + degrees;

        if (degrees > 0) { // right turn
            opMode.telemetry.addData("Right Turn", degrees);
            PivotRight(power);
            while (heading < endHeading) {
                heading = getCorrectZHeading();
                opMode.telemetry.addData("Heading", heading);
                opMode.telemetry.addData("End Heading", endHeading);

                if (timer.time() > seconds) {
                    stopMotors();
                    return false;
                }
            }
            opMode.telemetry.addData("Turn", "finished");
            stopMotors();
        } else if (degrees < 0) { // left turn
            opMode.telemetry.addData("Left Turn", degrees);
            PivotLeft(power);
            while (heading > endHeading) {
                heading = getCorrectZHeading();
                opMode.telemetry.addData("Heading", heading);

                if (timer.time() > seconds) {
                    stopMotors();
                    return false;
                }
            }
            opMode.telemetry.addData("Turn", "finished");
            stopMotors();
        }
        return true;
    }


    private boolean GoStraight(long distance, double power, int timeout) throws InterruptedException {
        int heading;
        Thread.sleep(500);
        gyroSensor.resetZAxisIntegrator();// set heading to zero
        opMode.waitForNextHardwareCycle();

        power = Math.abs(power);
        power = (distance < 0) ? -power : power;

        opMode.telemetry.addData("Heading", getCorrectZHeading());

        //currentPosition = getEncoderAverage();

        ElapsedTime timer = new ElapsedTime();

        if (power > 0) {
            opMode.telemetry.addData("go Forward", "");

            long currentPosition = rightMotor.getCurrentPosition();
            long targetPosition = currentPosition - distance;

            while ((currentPosition > targetPosition) && (timer.time() < timeout)) {
                heading = getCorrectZHeading();

                double correction = (double) heading / 180 * gain;

                double leftPower = power - correction;
                double rightPower = power + correction;

                leftPower = Range.clip(leftPower, -1, 1);
                rightPower = Range.clip(rightPower, -1, 1);

                leftMotor.setPower(leftPower);
                rightMotor.setPower(rightPower);

                currentPosition = rightMotor.getCurrentPosition();

                opMode.waitForNextHardwareCycle();

                opMode.telemetry.addData("Heading", heading);
                opMode.telemetry.addData("Correction(100X)", correction * 100);
                opMode.telemetry.addData("Right Motor Power(10X)", rightMotor.getPower() * 10);
                opMode.telemetry.addData("Left Motor Power(10X)", leftMotor.getPower() * 10);
                opMode.telemetry.addData("RtPos*", rightMotor.getCurrentPosition());
                opMode.telemetry.addData("LtPos", leftMotor.getCurrentPosition());
                opMode.telemetry.addData("TPos: ", targetPosition);
            }
            stopMotors();
        } else if (power < 0) {
            opMode.telemetry.addData("go Back", "");
            long currentPosition = leftMotor.getCurrentPosition();
            long targetPosition = currentPosition - distance;

            while ((currentPosition < targetPosition) && (timer.time() < timeout)) {
                heading = getCorrectZHeading();

                double correction = (double) heading / 180 * gain;

                double leftPower = power + correction;
                double rightPower = power - correction;

                leftPower = Range.clip(leftPower, -1, 1);
                rightPower = Range.clip(rightPower, -1, 1);

                leftMotor.setPower(leftPower);
                rightMotor.setPower(rightPower);

                currentPosition = leftMotor.getCurrentPosition();

                opMode.waitForNextHardwareCycle();

                opMode.telemetry.addData("Heading", heading);
                opMode.telemetry.addData("Correction(100X)", correction * 100);
                opMode.telemetry.addData("Right Motor Power(10X)", rightMotor.getPower() * 10);
                opMode.telemetry.addData("Left Motor Power(10X)", leftMotor.getPower() * 10);
                opMode.telemetry.addData("RtPos", rightMotor.getCurrentPosition());
                opMode.telemetry.addData("LtPos*", leftMotor.getCurrentPosition());
                opMode.telemetry.addData("TPos: ", targetPosition);
            }
            stopMotors();
        }
        return timer.time() <= timeout;
    }

    public boolean GoStraitInches(double inches, double power, int timeout) throws InterruptedException{
        opMode.telemetry.addData("GoInches", inches);
        long distance = (long) (inches / tireCircumference) * pulsesPerRevolution;
        return GoStraight(distance, power, timeout);
    }

    //Gyro Test
    public void GyroTest() {
        gyroSensor.resetZAxisIntegrator();

        while (true) {
            opMode.telemetry.addData("Heading", getCorrectZHeading());
        }
    }

    public void DistTest() {
        while (true) {
            opMode.telemetry.addData("Distance", distSensor.getLightDetected());
        }
    }

    private void StartMotors(double power) {
        rightMotor.setPower(power);
        leftMotor.setPower(power);
    }

    public boolean ApproachBeacon(int timeout) throws InterruptedException {
        ElapsedTime timer = new ElapsedTime();
        if (distSensor.getLightDetected() < .02) {
            opMode.telemetry.addData("No Light Detected", distSensor.getLightDetected());
            return false;
        }

        while (distSensor.getLightDetected() < .170) {
            opMode.telemetry.addData("Light Reflected", distSensor.getLightDetected());

            GoStraitInches(-1, .15, 2);
            Thread.sleep(1000);

            if (timer.time() > timeout) {
                opMode.telemetry.addData("Timeout", "");
                stopMotors();
                return false;
            }
        }
        stopMotors();
        return true;
    }
}
