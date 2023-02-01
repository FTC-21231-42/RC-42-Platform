package org.firstinspires.ftc.teamcode;


import static org.firstinspires.ftc.teamcode.RobotMap.*;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

@Autonomous(name = "Auto_0", group = "0")
public class Auto_0 extends LinearOpMode {
    static ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() throws InterruptedException {
        //init hardware map
        initRobot(hardwareMap);
        //default value
        IconTypeN targetPos = IconTypeN.NA;

        //init api
        initVuforia();
        initTFod();

        //activate tensorflow
        if (tFod != null) {
            tFod.activate();
            //ratio of camera
            tFod.setZoom(1.2, 16.0 / 9.0);
        }

        telemetry.addData("Initialized", "True");
        telemetry.update();
        waitForStart();

        //if can see anything after 5 seconds
        runtime.reset();
        while (opModeIsActive() && targetPos == IconTypeN.NA && runtime.seconds() < 8.0) {
            //detect icon
            targetPos = detectObject();
            sleep(20);

        }

        telemetry.addData("TargetPos", targetPos);
        telemetry.update();

        sleep(50);

        //TODO: Start writing here
        Move(90, 0.8, Direction.LEFT_RIGHT, 3);

        //move to stop position
        switch (targetPos) {
            case ONE:
                //TODO: Case 1

                break;

            case TWO:
                //TODO: Case 2

                break;

            case THREE:
                //TODO: Case 3

                break;

            case NA:
                //TODO: Case 4

                break;

        }

        sleep(2000);

    }

    /**
     * Initialize Vuforia
     * Camera API
     */
    private void initVuforia() {
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        //Name with the name of webcam on robot config
        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

    }

    /**
     * Initialize TensorFLow
     *
     */
    private void initTFod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        //minimum confidence rate to be recognized
        tfodParameters.minResultConfidence = 0.7f;
        //is a TensorFlow2 model
        tfodParameters.isModelTensorFlow2 = true;
        tfodParameters.inputSize = 320;
        tFod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        //Loading Model and Labels
        tFod.loadModelFromFile(TFOD_MODEL_ASSET, LABELS);

    }

    /**
     * Move the robot (Global Position Method)
     * axle (by power and direction): y+ being front, y- being back, x+ being left, x- being right
     * side moving alex: front direction being +
     * Unit: centimeter (cm)
     *
     * @param distance
     * @param power
     * @param direction FRONT_BACK, LEFT_RIGHT, SIDE_LEFT_RIGHT (left front to right bottom direction), SIDE_RIGHT_LEF T(right front to left bottom direction)
     * @param timeout
     */
    private void Move(double distance, double power, Direction direction, double timeout) {
        distance *= 17.5;
        double lfrbModify = 0;
        double rflbModify = 0;
        //different cases for different direction
        switch (direction) {
            case FRONT_BACK:
                lfrbModify = 1.0;
                rflbModify = 1.0;

                break;

            case LEFT_RIGHT:
                lfrbModify = -1.0;
                rflbModify = 1.0;

                break;

            case SIDE_FRONT_LEFT:
                lfrbModify = 0;
                rflbModify = 1.0;
                // 1 / sin45
                distance *= 1.414;

                break;

            case SIDE_FRONT_RIGHT:
                lfrbModify = 1.0;
                rflbModify = 0;
                // 1 / sin45
                distance *= 1.414;

                break;

            default:
                telemetry.addData("ERROR", "In Move()");
                telemetry.update();

                sleep(50);

        }

        lfPos -= (int) (distance * lfrbModify);
        rbPos += (int) (distance * lfrbModify);

        lbPos -= (int) (distance * rflbModify);
        rfPos += (int) (distance * rflbModify);

        //group 1
        leftFrontMotor.setTargetPosition(lfPos);
        rightBotMotor.setTargetPosition(rbPos);

        //group 2
        leftBotMotor.setTargetPosition(lbPos);
        rightFrontMotor.setTargetPosition(rfPos);

        //reset the time and set the power
        runtime.reset();
        leftFrontMotor.setPower(-power);
        rightBotMotor.setPower(power);
        leftBotMotor.setPower(-power);
        rightFrontMotor.setPower(power);

        //start the motors
        leftFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBotMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftBotMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        while (opModeIsActive()
                && runtime.seconds() < timeout
                && (leftFrontMotor.isBusy() || rightBotMotor.isBusy() || leftBotMotor.isBusy() || rightFrontMotor.isBusy())) {
            //holding the program till timeout or to target position

        }

        //stop and reset motors
        leftFrontMotor.setPower(0);
        rightBotMotor.setPower(0);
        leftBotMotor.setPower(0);
        rightFrontMotor.setPower(0);

        leftFrontMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBotMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBotMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFrontMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //cool down for the next function
        sleep(250);

    }

}
