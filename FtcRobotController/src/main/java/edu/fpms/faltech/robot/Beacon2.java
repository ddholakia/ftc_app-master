package edu.fpms.faltech.robot;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.DigitalChannelController;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by ddhol on 12/1/2015.
 */


public class Beacon2 {

    private LinearOpMode opMode;

    private ColorSensor colorSensor;
    private DeviceInterfaceModule cdim;
    private int redValue;
    private int blueValue;

    // we assume that the LED pin of the RGB sensor is connected to
    // digital port 0 (zero indexed).
    static final int LED_CHANNEL = 0;

    public Beacon2(LinearOpMode opMode, boolean ledEnabled) throws InterruptedException {
        this.opMode = opMode;
        opMode.telemetry.addData("constructor", "Beacon2");
        // get hardware mappings

        // get a reference to our DeviceInterfaceModule object.
        cdim = opMode.hardwareMap.deviceInterfaceModule.get("dim");

        // set the digital channel to output mode.
        // remember, the Adafruit sensor is actually two devices.
        // It's an I2C sensor and it's also an LED that can be turned on or off.
        cdim.setDigitalChannelMode(LED_CHANNEL, DigitalChannelController.Mode.OUTPUT);

        // get a reference to our ColorSensor object.
        colorSensor = opMode.hardwareMap.colorSensor.get("colorSensor");

        cdim.setDigitalChannelState(LED_CHANNEL, ledEnabled);

        // wait one cycle.
        opMode.waitOneFullHardwareCycle();
    }

    public void enableLED(boolean enableLED) {
        cdim.setDigitalChannelState(LED_CHANNEL, enableLED);
    }

    public int calibrateRed(int sampleSize) throws InterruptedException {
        int total = 0;
        int index = 0;

        while (index < sampleSize) {
            opMode.telemetry.addData("calibrate index", index);
            total += colorSensor.red();
            opMode.waitForNextHardwareCycle();
        }

        redValue = total / sampleSize;
        return redValue;
    }

    public int calibrateBlue(int sampleSize) throws InterruptedException {
        int total = 0;
        int index = 0;

        while (index < sampleSize) {
            opMode.telemetry.addData("calibrate index", index);
            total += colorSensor.blue();
            opMode.waitForNextHardwareCycle();
        }

        blueValue = total / sampleSize;
        return blueValue;
    }

    public boolean isRed() throws InterruptedException {
        opMode.waitOneFullHardwareCycle();
        return ((float) colorSensor.red() / redValue > .9) && ((float) colorSensor.blue() / blueValue < .6);
    }

    public void test() throws InterruptedException {
        opMode.waitOneFullHardwareCycle();
        opMode.telemetry.addData("Red", colorSensor.red());
        opMode.telemetry.addData("Blue", colorSensor.blue());
        opMode.telemetry.addData("Green", colorSensor.green());
    }
}
