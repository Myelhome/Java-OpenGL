package en.opengl.com.entity;

public class Vector3D {
    public double x;
    public double y;
    public double z;

    public Vector3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Vector3D sum(Vector3D a, Vector3D b) {
        return new Vector3D(a.x + b.x, a.y + b.y, a.z + b.z);
    }

    public static Vector3D diff(Vector3D a, Vector3D b) {
        return new Vector3D(a.x - b.x, a.y - b.y, a.z - b.z);
    }

    public static double dot(Vector3D a, Vector3D b) {
        return a.x * b.x + a.y * b.y + a.z * b.z;
    }

    public static Vector3D cross(Vector3D a, Vector3D b) {
        double tmpX = a.y * b.z - a.z * b.y;
        double tmpY = b.x * a.z - b.z * a.x;
        double tmpZ = a.x * b.y - a.y * b.x;

        return new Vector3D(tmpX, tmpY, tmpZ);
    }

    public Vector3D normalize() {
        double newX = this.x / length();
        double newY = this.y / length();
        double newZ = this.z / length();

        return new Vector3D(newX, newY, newZ);
    }

    public double length() {
        return Math.sqrt(this.x*this.x + this.y*this.y + this.z*this.z);
    }
}