package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "BigIron", group = "SCC")
public class BigIron extends LinearOpMode
{
    // Create motor and servo objects
    private DcMotor launchMotor;
    private DcMotor liftMotor;
    private Servo feedServo;
    private Servo clawServo;

    private DcMotor leftFrontMotor;
    private DcMotor rightFrontMotor;
    private DcMotor leftRearMotor;
    private DcMotor rightRearMotor;

    // Vars
    double slowDriveFactor = 1.0;

    @Override
    public void runOpMode() throws InterruptedException
    {
        // Setup motors and servos
        launchMotor = hardwareMap.dcMotor.get("launchMotor");
        liftMotor = hardwareMap.dcMotor.get("liftMotor");
        feedServo = hardwareMap.servo.get("feedServo");
        clawServo = hardwareMap.servo.get("clawServo");

        leftFrontMotor = hardwareMap.dcMotor.get("leftFrontMotor");
        rightFrontMotor = hardwareMap.dcMotor.get("rightFrontMotor");
        leftFrontMotor.setDirection(DcMotor.Direction.REVERSE);
        rightFrontMotor.setDirection(DcMotor.Direction.REVERSE);
        leftRearMotor = hardwareMap.dcMotor.get("leftRearMotor");
        rightRearMotor = hardwareMap.dcMotor.get("rightRearMotor");

        waitForStart();

        while (opModeIsActive()) {
            // Service the driver operator's inputs
            double motorFR = 0;
            double motorFL = 0;
            double motorBR = 0;
            double motorBL = 0;
            final float threshold = 0.2f;
            if (Math.abs(gamepad2.left_stick_y) > threshold || Math.abs(gamepad2.left_stick_x) > threshold) {
                motorFR = (gamepad2.left_stick_y - gamepad2.left_stick_x) / 2;
                motorFL = (-gamepad2.left_stick_y - gamepad2.left_stick_x) / 2;
                motorBR = (-gamepad2.left_stick_y - gamepad2.left_stick_x) / 2;
                motorBL = (gamepad2.left_stick_y - gamepad2.left_stick_x) / 2;
            }
            if (Math.abs(gamepad2.left_stick_x) > threshold) {
                motorFR *= -1;
                motorFL *= -1;
                motorBR *= -1;
                motorBL *= -1;
            }
            if (Math.abs(gamepad2.right_stick_x) > threshold) {
                //rotate
                motorFR = (gamepad2.right_stick_x) / 2;
                motorFL = (gamepad2.right_stick_x) / 2;
                motorBR = (-gamepad2.right_stick_x) / 2;
                motorBL = (-gamepad2.right_stick_x) / 2;
            }
            /*if (gamepad2.a) {
                slowDriveFactor = 0.3;
            } else {
                slowDriveFactor = 1.0;
            }*/
            leftFrontMotor.setPower(motorFL * slowDriveFactor);
            rightFrontMotor.setPower(motorFR * slowDriveFactor);
            leftRearMotor.setPower(motorBL * slowDriveFactor);
            rightRearMotor.setPower(motorBR * slowDriveFactor);






            // Service right hand operation of BIG IRON
            if (gamepad1.y) {
                // Move claw servo to home position
                clawServo.setPosition(0);

                // Turn launch motor on to fire ring
                launchMotor.setPower(-1.0);

                // Wait 1 second before launching ring to get launch motor up to full speed
                sleep(1000);

                // Push ring into launcher with feed servo
                feedServo.setPosition(0.5);
            } else if (gamepad1.a) {
                // Put feed servo into zero position
                feedServo.setPosition(0.35);

                // Feed ring into launcher with claw servo
                clawServo.setPosition(0.5);

                // Pick up ring by starting launch motor in reverse
                launchMotor.setPower(1.0);
            } else {
                launchMotor.setPower(0);
            }

            // Service left hand operation of BIG IRON
            if (gamepad1.dpad_up) {
                liftMotor.setPower(1.0);
            } else if (gamepad1.dpad_down) {
                liftMotor.setPower(-0.2);
            } else {
                liftMotor.setPower(0.1);
            }

            idle();
        }
    }
}