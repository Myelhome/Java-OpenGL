package en.opengl.com.entity;

public class Vector2D {
    public double x;
    public double y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static Vector2D toVector2D(Vector3D vector3D) {
        return new Vector2D((int) vector3D.x, (int) vector3D.y);
    }
}
