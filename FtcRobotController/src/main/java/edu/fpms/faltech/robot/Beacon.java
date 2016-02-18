/* Copyright (c) 2015 Qualcomm Technologies Inc

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Qualcomm Technologies Inc nor the names of its contributors
may be used to endorse or promote products derived from this software without
specific prior written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package edu.fpms.faltech.robot;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.LED;

public class Beacon {
    private LinearOpMode opMode;
    private ColorSensor colorSensor;
    private DeviceInterfaceModule cdim;
    private LED led;

    public Beacon(LinearOpMode opMode, boolean enableLed) throws InterruptedException {
        this.opMode = opMode;
        opMode.telemetry.addData("mtthd: ", "Beacon constructor");

        // Identify Beacon Sensor from hardware map
        opMode.hardwareMap.logDevices();
        cdim = opMode.hardwareMap.deviceInterfaceModule.get("dim");
        colorSensor = opMode.hardwareMap.colorSensor.get("beacon");

        // Switch LED ON or OFF
        led.enable(enableLed);

        // TouchSensor t;
        // private void enable(boolean enableLed) {
        //  switch (device) {
        //    case HITECHNIC_NXT:
        //      colorSensor.enableLed(enableLed);
        //      break;
        //    case ADAFRUIT:
        //      led.enable(enableLed);
        //      break;
        //    case MODERN_ROBOTICS_I2C:
        //      colorSensor.enableLed(enableLed);
        //      break;
        //  }
    }

    // Function to check if the beacon is red
    public boolean isRed() {
        //waitForStart();
        float hsvValues[] = {0, 0, 0};
        final float values[] = hsvValues;
        // final View relativeLayout = ((Activity) hardwareMap.appContext).findViewById(R.id.RelativeLayout);
        //while (opModeIsActive()) {

        Color.RGBToHSV((colorSensor.red() * 255) / 800, (colorSensor.green() * 255) / 800, (colorSensor.blue() * 255) / 800, hsvValues);

        opMode.telemetry.addData("Clear", colorSensor.alpha());
        opMode.telemetry.addData("Red  ", colorSensor.red());
        opMode.telemetry.addData("Green", colorSensor.green());
        opMode.telemetry.addData("Blue ", colorSensor.blue());
        opMode.telemetry.addData("Hue", hsvValues[0]);
        //relativeLayout.post(new Runnable() {
        //  public void run() {
        //    relativeLayout.setBackgroundColor(Color.HSVToColor(0xff, values));
        //  }
        // });
        // 0waitOneFullHardwareCycle();

        //}

        //identify red from hsv table, (hue= 0), adding additional range

        if (hsvValues[0] >= 330 & hsvValues[0] <= 360) {
            return true;
        } else return hsvValues[0] <= 30 & hsvValues[0] >= 0;
    }
}