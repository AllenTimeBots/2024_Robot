package frc.robot.subsystems.Vision;

import edu.wpi.first.wpilibj.Relay.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants.VisionConstants;
import frc.robot.CustomTypes.Math.SillyMath;
import frc.robot.CustomTypes.Math.Vector2;
import frc.robot.CustomTypes.Math.Vector3;

public class VisionDriveCalculator {
    public static Vision vision;

    public static double max = 0;

    public static double min = 0;

    public static boolean wasNan = false;

    public static void SetVisionReference(Vision vision) { VisionDriveCalculator.vision = vision; }

    public static double rotateTowardsTarget(double offset)
    {
        double x = (vision.HorizontalOffsetFromAprilTag() + offset);
        int sign = x > 0 ? 1 : -1;
        double value = -SillyMath.clamp(((Math.pow(x,2)) / ((Math.abs(x)+100)*sign)) , -2, 2);

        if (!wasNan&&Double.isNaN(value)) {
            wasNan = true;
            System.out.println("Was NAN\n" + x + "\n" + value + "\n" + offset);
        }
        if(wasNan){
            return 0;
        }
        SmartDashboard.putBoolean("Was Nan", wasNan);
        if (value > max) {
            max = value;
            SmartDashboard.putNumber("Max thing", value);
        }
        if (value < min) {
            min = value;
            SmartDashboard.putNumber("Min thing", value);
        }
        return value;
    }

    public static AprilTagMoveVelocity GetVelocityToAprilTagOffset(Vector2 aprilTagOffset)
    {
        if (vision.hasValidData())
        {
            Vector2 adjustedAprilTagOffset = new Vector2(aprilTagOffset.x, aprilTagOffset.y); // needed because otherwise doesnt work right for some reason
            Vector2 aprilTagOffsetInRobotSpace = Vector2.rotate(adjustedAprilTagOffset, Math.toRadians(-vision.AprilTagRotInRobotSpace().y + 180));
            Vector2 aprilTagInRobotSpace = new Vector2(vision.AprilTagPosInRobotSpace().x, vision.AprilTagPosInRobotSpace().z);
            Vector2 targetPositionInRobotSpace = aprilTagInRobotSpace.add(aprilTagOffsetInRobotSpace);

            SmartDashboard.putString("AT offset in RS", aprilTagOffsetInRobotSpace.toString(3));
            SmartDashboard.putString("AT in RS", aprilTagInRobotSpace.toString(3));
            SmartDashboard.putString("AT Target Position in RS", targetPositionInRobotSpace.toString(3));
            SmartDashboard.putNumber("AT Target Distance", targetPositionInRobotSpace.magnitude());

            //return new AprilTagMoveVelocity(Vector2.zero, 0, false);
            return new AprilTagMoveVelocity(ApplyEasingToVector2(targetPositionInRobotSpace), targetPositionInRobotSpace.magnitude(), true);
        }

        return new AprilTagMoveVelocity(Vector2.zero, 0, false);
    }

    static Vector2 ApplyEasingToVector2(Vector2 direction)
    {
        //double easedMagnitude = Math.cbrt(direction.magnitude() + 1) - 1;
        //double easedMagnitude = Math.pow(direction.magnitude() + 1, 1/2.5) - 1;
        double easedMagnitude = Math.sqrt(direction.magnitude() + 1) - 1;

        return direction.normalized().times(easedMagnitude);
    }

    public static double getAngleOffsetForVision()
    {
        if (vision.hasValidData())
        {
            Vector3 aprilTagPos = vision.AprilTagPosInRobotSpace();
            Vector2 aprilTagHorizontalPos = new Vector2(aprilTagPos.x, aprilTagPos.z);


            double limelightDistToAprilTag = aprilTagHorizontalPos.magnitude();

            //return (4.75 / (limelightDistToAprilTag * limelightDistToAprilTag)) + 9;
            double value = (14 / Math.pow(limelightDistToAprilTag, 0.7));//(4.75 / (limelightDistToAprilTag * limelightDistToAprilTag)) + 9;
            if(Double.isInfinite(value)){
                System.out.println("Dis to april tag: " + limelightDistToAprilTag);
            }
            return value;
        }

        return 0;
    }

    public static class AprilTagMoveVelocity
    {
        public Vector2 velocity;
        public double distanceFromTarget;
        public boolean validData;

        public AprilTagMoveVelocity(Vector2 velocity, double distanceFromTarget, boolean validData)
        {
            this.velocity = velocity;
            this.distanceFromTarget = distanceFromTarget;
            this.validData = validData;
        }
    }
}
