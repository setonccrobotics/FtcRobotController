package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp(name = "BigIron", group = "SCC")
public class BigIron extends LinearOpMode
{
    // Create motor and servo objects
    private DcMotorEx launchMotor;
    private DcMotor liftMotor;
    private Servo feedServo;
    private Servo clawServo;

    private DcMotor leftFrontMotor;
    private DcMotor rightFrontMotor;
    private DcMotor leftRearMotor;
    private DcMotor rightRearMotor;

    // Vars
    double slowDriveFactor = 1.0;
    //int towerGoalPosition = 670;
    int towerGoalPosition = 685;
    double clawHomePosition = 0.1;
    double clawFeedPosition = 0.8;
    double clawRestPosition = 0.4;

    double tempServoPos = clawHomePosition;

    @Override
    public void runOpMode() throws InterruptedException
    {
        // Setup and zero robot
        setupMotorsAndServos();
        zeroLiftEncoder();
        feedServo.setPosition(0.1);
        clawServo.setPosition(clawHomePosition);
        launchMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Wait for the captain to press start on the cell phone
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

            /*if (gamepad1.b) {
                // Debug servo
                tempServoPos += 0.1;
                clawServo.setPosition(tempServoPos);
                sleep(1000); //Poorman's debounce
            } else if (gamepad1.x) {
                tempServoPos -= 0.1;
                clawServo.setPosition(tempServoPos);
                sleep(1000); //Poorman's debounce
            }
            telemetry.addData("tempServoPos",  "%.3f", tempServoPos);
            telemetry.update();*/
            if (gamepad1.b) {
                // Debug servo
                towerGoalPosition += 5;
                sleep(500); //Poorman's debounce
            } else if (gamepad1.x) {
                towerGoalPosition -= 5;
                sleep(500); //Poorman's debounce
            }
            updateTelemetry();

            // Service right hand operation of BIG IRON
            if (gamepad1.y) {
                // Lift launcher into top position
                liftToEncoderPosition(towerGoalPosition);

                // Move claw servo to home position
                clawServo.setPosition(clawHomePosition);

                // Turn launch motor on to fire ring
                //launchMotor.setPower(-1.0);
                launchMotor.setVelocity(-2200.0);
                while (launchMotor.getVelocity() > -2200) {
                    sleep(20);
                }
                updateTelemetry();

                // Push ring into launcher with feed servo
                feedServo.setPosition(0.4);
            } else if (gamepad1.a) {
                // Put feed servo into zero position
                feedServo.setPosition(0.1);

                // Pick up ring by starting launch motor in reverse
                //launchMotor.setPower(1.0);
                launchMotor.setVelocity(2200);
                while (launchMotor.getVelocity() < 2200) {
                    sleep(20);
                }
                updateTelemetry();

                // Feed ring into launcher with claw servo
                clawServo.setPosition(clawFeedPosition);

                // Wait 1 second before launching ring to get launch motor up to full speed
                sleep(500);
            } else {
                //launchMotor.setPower(0);
                launchMotor.setVelocity(0.0);
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

    public void setupMotorsAndServos()
    {
        // Setup motors and servos
        launchMotor = hardwareMap.get(DcMotorEx.class, "launchMotor");
        liftMotor = hardwareMap.dcMotor.get("liftMotor");
        liftMotor.setDirection(DcMotor.Direction.REVERSE);
        feedServo = hardwareMap.servo.get("feedServo");
        clawServo = hardwareMap.servo.get("clawServo");

        leftFrontMotor = hardwareMap.dcMotor.get("leftFrontMotor");
        rightFrontMotor = hardwareMap.dcMotor.get("rightFrontMotor");
        leftFrontMotor.setDirection(DcMotor.Direction.REVERSE);
        rightFrontMotor.setDirection(DcMotor.Direction.REVERSE);
        leftRearMotor = hardwareMap.dcMotor.get("leftRearMotor");
        rightRearMotor = hardwareMap.dcMotor.get("rightRearMotor");
    }
    public void zeroLiftEncoder()
    {
        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Resetting Encoders");
        telemetry.update();
        liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        liftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void liftToEncoderPosition(int position)
    {
        liftMotor.setTargetPosition(position);
        liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftMotor.setPower(1.0);

        while (opModeIsActive() && liftMotor.isBusy()) {
            updateTelemetry();
            idle();
        }
        liftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        liftMotor.setPower(0.1);
    }

    public void updateTelemetry()
    {
        telemetry.addData("towerGoalPosition", towerGoalPosition);
        telemetry.addData("launchMotorVelocity", launchMotor.getVelocity());
        telemetry.addData("liftMotorPosition", liftMotor.getCurrentPosition());
        telemetry.update();
    }
}
