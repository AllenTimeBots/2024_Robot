package frc.robot.subsystems.Vision;

public class AutoTargetStateManager {
    public static boolean isAutoTargeting = false;

    public static void toggleAutoTarget() {
        isAutoTargeting = !isAutoTargeting;
        Vision.Instance.setUsingLimelight(isAutoTargeting);
    }

    public static void onStart()
    {
        isAutoTargeting = false;
    }
}
