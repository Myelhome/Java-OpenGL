package en.opengl.com.params;

public class Properties {
    public static final int w = 1366;
    public static final int h = 768;
    public static final long framerate = 1000 / 60;
    public static String modelpath;
    public static String texturepath;
    public static double thetaX = 0;
    public static double thetaY = 0;
    public static double thetaZ = 0;
    public static double scale = 1;
    public static double shiftX = 0;
    public static double shiftY = 0;
    public static double shiftZ = 0;
    public static int lightX = 0;
    public static int lightY = 0;
    public static int lightZ = 1000;
    public static double stretchX = 0;
    public static double stretchY = 0;
    public static double stretchZ = 0;
    public static double speed = 1 / 20.0;
    public static double speedShift = 10;
    public static double speedLight = 50;
    public static double fNear = 0.1;
    public static double fFar = 1000.0;
    public static double fFov = 90.0;
    public static double fAspectRatio = h / (double) w;
    public static double fFovRad = 1.0 / Math.tan(fFov * 0.5 / 180.0 * Math.PI);
}
