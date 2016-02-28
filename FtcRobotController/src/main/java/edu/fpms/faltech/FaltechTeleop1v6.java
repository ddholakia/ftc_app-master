
/*
FALTECH 7079
FOUR POINTS MIDDLE SCHOOL
Thanks to Brendan Hollaway, from 6209 Venom
 */
package edu.fpms.faltech;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * TeleOp Mode
 * <p/>
 * Enables control of the robot via the gamepad
 */
public class FaltechTeleop1v6 extends OpMode {
	int HopperShuffleValue = 1;
	double SpinnersValue = 0;
	boolean isReversed = false;
	boolean btn_pressed = false;
	long timer = 0;

	//Motors
	DcMotor ArmRight;
	DcMotor ArmLeft;
	DcMotor MtrsLeft;
	DcMotor MtrsRight;
	DcMotor Elevator;
	DcMotor Spinners;

	//Servos
	Servo ChurroGrab1;
	Servo ChurroGrab2;
	Servo HopperSrv;
	Servo climberSaverServo;

	//Motor Power Settings
	float MtrsLeftPower;
	float MtrsRightPower;
	float ArmRightPower;
	float ArmLeftPower;


	public FaltechTeleop1v6() {


	}

	/*
     * Code to run when the op mode is first enabled goes here
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
     */
	@Override
	public void init() {
		ArmRight = hardwareMap.dcMotor.get("ArmRight");
		ArmLeft = hardwareMap.dcMotor.get("ArmLeft");
		MtrsLeft = hardwareMap.dcMotor.get("leftMotor");
		MtrsRight = hardwareMap.dcMotor.get("rightMotor");
		MtrsRight.setDirection(DcMotor.Direction.REVERSE);
		Spinners = hardwareMap.dcMotor.get("Spinners");
		Elevator = hardwareMap.dcMotor.get("Elevator");
		HopperSrv = hardwareMap.servo.get("HopperSrv");
		ChurroGrab1 = hardwareMap.servo.get("ChurroGrab1");
		ChurroGrab2 = hardwareMap.servo.get("ChurroGrab2");


		//Set Churro Grabber's Position
		ChurroGrab1.setPosition(1);
		ChurroGrab2.setPosition(0);
		HopperSrv.setPosition(.5);

	}


	/*
     * This method will be called repeatedly in a loop
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#run()
     */
	@Override
	public void loop() {

		//Drive Train
		this.telemetry.addData("isReversed", isReversed);
		if (System.nanoTime() > timer)
			btn_pressed = false;
		if (!btn_pressed && gamepad1.b) { // Toggle Backwards Driving
			isReversed = !isReversed;
			timer = System.nanoTime() + (long) (0.5 * Math.pow(10, 9));
			btn_pressed = true;
		}
		if (isReversed) {
			MtrsLeftPower = -gamepad1.left_stick_y;
			MtrsRightPower = -gamepad1.right_stick_y;
		} else {
			MtrsLeftPower = gamepad1.right_stick_y;
			MtrsRightPower = gamepad1.left_stick_y;
		}

		//Arms
        /*
        if (gamepad2.right_trigger > 50) { //If RT is pressed, full-power mode
            ArmRightPower = gamepad2.right_stick_y;
            ArmLeftPower = gamepad2.right_stick_y;
            telemetry.addData("ArmPower Full", gamepad2.right_stick_y);
        }
		else if (gamepad2.right_trigger <= 50 ) { //IF RT is not pressed, half-power mode
            ArmRightPower = gamepad2.right_stick_y / 2;
            ArmLeftPower = gamepad2.right_stick_y / 2;
            telemetry.addData("ArmPower Half", gamepad2.right_stick_y);
        }
        */

		ArmRightPower = gamepad2.right_stick_y;
		ArmLeftPower = -gamepad2.right_stick_y;
		telemetry.addData("ArmPower Full", gamepad2.right_stick_y);


		//Elevator

		float ElevatorFloat = gamepad2.left_stick_y;

		//setting motors power
		MtrsRight.setPower(MtrsRightPower);
		MtrsLeft.setPower(MtrsLeftPower);
		ArmLeft.setPower(ArmLeftPower);
		ArmRight.setPower(ArmRightPower);
		Elevator.setPower(ElevatorFloat);

		//Hoppers
		if (gamepad2.left_bumper || gamepad2.right_bumper) {
			if (gamepad2.left_bumper) {
				HopperSrv.setPosition(0);
			} else {//rightbumper
				HopperSrv.setPosition(1);
			}
		} else if (!gamepad2.left_bumper || !gamepad2.right_bumper) {
			HopperSrv.setPosition(.5);
		}

		//Churro Grabbers
		if (gamepad1.left_bumper || gamepad1.right_bumper) {  //Churro Grabbers
			if (gamepad1.right_bumper) { //disengadge Churro Grabbers
				ChurroGrab1.setPosition(0);
				ChurroGrab2.setPosition(1);
				this.telemetry.addData("ChurroGrabbers", " Right");
			} else if (gamepad1.left_bumper) { //engadge Churro Grabbers
				ChurroGrab1.setPosition(1);
				ChurroGrab2.setPosition(0);
				this.telemetry.addData("ChurroGrabbers", " Left");
			}
		}

		//Spinners
		if ((gamepad1.right_trigger > .50) || (gamepad1.left_trigger > .50)) {
			if (gamepad1.right_trigger > .50) { //If Right trigger then collect
				Spinners.setPower(-1.0);
				this.telemetry.addData("Spinners", "In");
			} else if (gamepad1.left_trigger > .50) { //if left trigger then flush
				Spinners.setPower(1.0);
				this.telemetry.addData("Spinners", "Out");
			}

		} else if (!(gamepad1.right_trigger > .50) || !(gamepad1.left_trigger > 50)) {
			Spinners.setPower(0);
		}
		telemetry.addData("Right Speed", -gamepad1.right_stick_y);
		telemetry.addData("Left Speed", -gamepad1.left_stick_y);

		//climberSaverServo
		if (gamepad1.dpad_up || gamepad1.dpad_left){
			if (gamepad1.dpad_up){
				climberSaverServo.setPosition(.5);
			}else{
				climberSaverServo.setPosition(0);
			}
		}

	}

	/*
     * Code to run when the op mode is first disabled goes here
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
     */
	@Override
	public void stop() {

	}

}
