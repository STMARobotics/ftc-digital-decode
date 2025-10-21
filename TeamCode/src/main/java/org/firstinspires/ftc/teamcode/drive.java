package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

import java.util.Timer;
import java.util.concurrent.TimeUnit;

@TeleOp(name="drive", group="Linear OpMode")
public class drive extends LinearOpMode {

    private ElapsedTime shootTimer = new ElapsedTime();
    private boolean isShooting = false;
    final double LAUNCHER_TARGET_VELOCITY = 1800;
    final double LAUNCHER_MIN_VELOCITY = 1750;
    private DcMotorEx Launcher = null;
    private CRServo LeftServo = null;
    private CRServo RightServo = null;

    private enum LaunchState {
        IDLE,
        SPIN_UP,
        LAUNCH,
        LAUNCHING,
    }

    private LaunchState launchState;

    void launch(boolean shotRequested) {
        switch (launchState) {
            case IDLE:
                if (shotRequested) {
                    launchState = LaunchState.SPIN_UP;
                }
                break;
            case SPIN_UP:
                Launcher.setVelocity(LAUNCHER_TARGET_VELOCITY);
                if (Launcher.getVelocity() > LAUNCHER_MIN_VELOCITY) {
                    launchState = LaunchState.LAUNCH;
                    LeftServo.setPower(-0.1);
                    RightServo.setPower(-0.1);
                }
                break;
            case LAUNCH:
                LeftServo.setPower(.5);
                RightServo.setPower(.5);
                shootTimer.reset();
                launchState = LaunchState.LAUNCHING;
                break;
            case LAUNCHING:
                if (shootTimer.seconds() > .02) {
                    launchState = LaunchState.IDLE;
                    LeftServo.setPower(0);
                    RightServo.setPower(0);
                    Launcher.setPower(0);
                }
                break;
        }
    }

    @Override
    public void runOpMode() {
        launchState = LaunchState.IDLE;

        // step (using the FTC Robot Controller app on the phone).
        DcMotor frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        DcMotor rearLeft = hardwareMap.get(DcMotor.class, "rearLeft");
        DcMotor frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        DcMotor rearRight = hardwareMap.get(DcMotor.class, "rearRight");
        Launcher = hardwareMap.get(DcMotorEx.class, "Launcher");
        RightServo = hardwareMap.get(CRServo.class, "RightServo");
        LeftServo = hardwareMap.get(CRServo.class, "LeftServo");

        Launcher.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(300, 0, 0, 10));
        Launcher.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        rearLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        RightServo.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();
        while (opModeIsActive()) {
            double y = gamepad1.left_stick_y;
            double x = gamepad1.left_stick_x;
            double rx = gamepad1.right_stick_x;

            // Calculations
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontRightPower = (y + x + rx) / denominator;
            double rearRightPower = (y - x + rx) / denominator;
            double rearLeftPower = (y + x - rx) / denominator;
            double frontLeftPower = (y - x - rx) / denominator;
            //How you set the power you feel
            frontLeft.setPower(frontLeftPower);
            rearLeft.setPower(rearLeftPower);
            frontRight.setPower(rearRightPower);
            rearRight.setPower(frontRightPower);
            //the servo thingey
            launch(gamepad1.right_trigger > 0.1);

            if (gamepad1.right_bumper) {
                RightServo.setPower(0);
                LeftServo.setPower(0);
                Launcher.setPower(0);
                launchState = LaunchState.IDLE;
            }

            telemetry.addData("speed", Launcher.getVelocity());
            telemetry.update();


        }
    }

};