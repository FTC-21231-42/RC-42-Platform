package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

public class RobotMap {
    //TODO initialize variables
    //TODO KEEP EVERYTHING IN VARIABLES

    //tf and vuforia
    static final String VUFORIA_KEY = "AQMzFW3/////AAABmbeNyVU2Y0RtubAsx+MyOM41z7gags/qzhp4KLi3oPaw4cNKJHv0bGKmQUZUjuqDQfMIwJaiagRRZ8HC+FQAFWwtSQ4NbkAykIX+BlfH9uMbIvXAQrQTk18QFAKsixYyrDXTRKeEFiLr5ppKs1HF0w8awj6HAzwSoIf/h1JJPLsc1ch4GQiHg1KnBMiDMRvmpFKAQxdNEF11QaE/E4P+6KE0wslmhD2iIK0VtBYevOvBRC3kaS0LfM0dy2JxikfOEgUkK/yE8IaYJp2xCZt4b13aglweW9LesT5oZMe2zb8S7GfP+ZLEddFKbKnd7eWqkzcVNvMIliWhGocOZKr3aIvuJCJbS9Sc7OEk7fUTdthK";

    static final String TFOD_MODEL_ASSET = "/sdcard/FIRST/tflitemodels/model_c1.tflite";

    static final String[] LABELS = {
            "one",
            "three",
            "two"
    };

    static IconTypeN targetPos = IconTypeN.NA;

    static VuforiaLocalizer vuforia;
    static TFObjectDetector tFod;

    static DcMotor leftFrontMotor, rightFrontMotor, leftBotMotor, rightBotMotor;

    //Global position of motors
    static int lfPos, rbPos, lbPos, rfPos;

    enum IconTypeN {
        ONE,
        TWO,
        THREE,
        NA

    }

    enum Direction {
        FRONT_BACK,
        LEFT_RIGHT,
        SIDE_FRONT_LEFT,
        SIDE_FRONT_RIGHT

    }

    /**
     * Initialize the hardware map of the robot
     * in EVERY program, run this function first in the initialize part
     * @param map type in hardwareMap
     */
    public static void initRobot(HardwareMap map) {

        //Motors
        leftFrontMotor = map.get(DcMotor.class, "leftFrontMotor");
        rightFrontMotor = map.get(DcMotor.class, "rightFrontMotor");
        leftBotMotor = map.get(DcMotor.class, "leftBotMotor");
        rightBotMotor = map.get(DcMotor.class, "rightBotMotor");

        //reset the global position
        lfPos = 0;
        rbPos = 0;
        lbPos = 0;
        rfPos = 0;

        //Init hardware
        leftFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBotMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBotMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftFrontMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFrontMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBotMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBotMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        leftFrontMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFrontMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBotMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBotMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

    }

    /**
     * Object detecting function, this one uses the recognition.getLabel() to decide the case
     * Also can use recognition.getLeft(), recognition.getTop(), etc. to decide the case
     * @return
     */
    public static IconTypeN detectObject() {
        List<Recognition> updatedRecognitions = tFod.getUpdatedRecognitions();
        if (updatedRecognitions != null) {
            for (Recognition recognition : updatedRecognitions) {
                switch (recognition.getLabel()) {
                    case "one":
                        return IconTypeN.ONE;

                    case "two":
                        return IconTypeN.TWO;

                    case "three":
                        return IconTypeN.THREE;

                    default:
                        break;

                }

            }
        }
        //if none case was fitted, return NA
        return IconTypeN.NA;

    }

}


