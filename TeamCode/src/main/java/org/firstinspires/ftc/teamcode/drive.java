package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.Timer;
import java.util.concurrent.TimeUnit;

@TeleOp(name="drive", group="Linear OpMode")
public class drive extends LinearOpMode {

    private ElapsedTime shootTimer = new ElapsedTime();
    private boolean isShooting = false;

    @Override
    public void runOpMode() {

        // step (using the FTC Robot Controller app on the phone).
        DcMotor frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        DcMotor rearLeft = hardwareMap.get(DcMotor.class, "rearLeft");
        DcMotor frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        DcMotor rearRight = hardwareMap.get(DcMotor.class, "rearRight");
        DcMotor Launcher = hardwareMap.get(DcMotor.class, "Launcher");
        CRServo RightServo = hardwareMap.get(CRServo.class, "RightServo");
        CRServo LeftServo = hardwareMap.get(CRServo.class, "LeftServo");

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        rearLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        RightServo.setDirection(DcMotorSimple.Direction.REVERSE);


        waitForStart();
        while (opModeIsActive()) {
            double y = gamepad1.left_stick_y;
            double x = gamepad1.left_stick_x;
            double rx = gamepad1.right_stick_x;
            double rt = gamepad1.right_trigger * .2;

            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontRightPower = (y + x + rx) / denominator;
            double rearRightPower = (y - x + rx) / denominator;
            double rearLeftPower = (y + x - rx) / denominator;
            double frontLeftPower = (y - x - rx) / denominator;

            frontLeft.setPower(frontLeftPower);
            rearLeft.setPower(rearLeftPower);
            frontRight.setPower(rearRightPower);
            rearRight.setPower(frontRightPower);
            Launcher.setPower(rt);
            //Toggles FlyWheel
            if (gamepad1.right_trigger >= 0.1) {
                Launcher.setPower(0.5);
                telemetry.addLine("SPINNING");
            }
            if (gamepad1.right_bumper) {
                Launcher.setPower(0);
                telemetry.addLine("STOPPING");
            }

            if (gamepad1.left_trigger >= 0.1) {
                if (!isShooting) {

                    shootTimer.reset();
                }

                if (shootTimer.milliseconds() % 750 < 250) {
                    LeftServo.setPower(1);
                    RightServo.setPower(1);
                    telemetry.addLine("RUNNING FEEDER");
                } else {
                    LeftServo.setPower(0);
                    RightServo.setPower(0);
                    telemetry.addLine("PAUSING FEEDER");
                }
                isShooting = true;
            } else {
                isShooting = false;
                LeftServo.setPower(0);
                RightServo.setPower(0);
            }

        }
    }
}