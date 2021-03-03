package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "ShrekTheRobot", group = "SCC")
public class ShrekTheRobot extends LinearOpMode
{
    // Create motor and servo objects
    private DcMotor frontRollerMotor;
    private DcMotor bristletoothMotor;
    private DcMotorEx launchMotor;

    private Servo leftFeedServo;
    private Servo rightFeedServo;

    @Override
    public void runOpMode() throws InterruptedException
    {
        // Setup and zero robot
        setupMotorsAndServos();
        zeroLiftEncoder();

        // Wait for the captain to press start on the cell phone
        waitForStart();

        while (opModeIsActive()) {
            frontRollerMotor.setPower(-1.0);
            bristletoothMotor.setPower(-1.0);
            launchMotor.setVelocity(-2200.0);

            // Fire servos
            if (gamepad1.a) {
                leftFeedServo.setPosition(0.4);
                rightFeedServo.setPosition(0.6);
                sleep(1000);
                leftFeedServo.setPosition(0.82);
                rightFeedServo.setPosition(0.1);
                sleep(1000);
            }

            idle();
        }
    }

    public void setupMotorsAndServos()
    {
        // Setup motors and servos
        frontRollerMotor = hardwareMap.dcMotor.get("frontRollerMotor");
        bristletoothMotor = hardwareMap.dcMotor.get("bristletoothMotor");
        launchMotor = hardwareMap.get(DcMotorEx.class, "launchMotor");

        leftFeedServo = hardwareMap.servo.get("leftFeedServo");
        rightFeedServo = hardwareMap.servo.get("rightFeedServo");
    }
    public void zeroLiftEncoder()
    {
        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Resetting Encoders");
        telemetry.update();

        leftFeedServo.setPosition(0.82);
        rightFeedServo.setPosition(0.1);
    }

    public void updateTelemetry()
    {
        //telemetry.addData("frontRollerMotor", frontRollerMotor);
        //telemetry.update();
    }
}