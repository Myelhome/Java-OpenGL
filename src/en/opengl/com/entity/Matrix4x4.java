package en.opengl.com.entity;

public class Matrix4x4 {
    public double[][] m = new double[4][4];

    public Matrix4x4() {
    }

    public Matrix4x4(double[][] m) {
        this.m = m;
    }

    public static Matrix4x4 getProjectionMatrix(double fNear, double fFar, double fAspectRatio, double fFovRad) {
        Matrix4x4 matrixProj = new Matrix4x4();
        matrixProj.m[0][0] = fAspectRatio * fFovRad;
        matrixProj.m[1][1] = fFovRad;
        matrixProj.m[2][2] = fFar / (fFar - fNear);
        matrixProj.m[3][2] = (-fFar * fNear) / (fFar - fNear);
        matrixProj.m[2][3] = 1.0;
        return matrixProj;
    }

    public static Matrix4x4 getMultiplicationMatrix(double scale){
        Matrix4x4 matrixMult = new Matrix4x4();
        matrixMult.m[0][0] = scale;
        matrixMult.m[1][1] = scale;
        matrixMult.m[2][2] = scale;
        matrixMult.m[3][3] = 1;
        return  matrixMult;
    }

    public static Matrix4x4 getStretchMatrix(double stretchX, double stretchY, double stretchZ){
        Matrix4x4 matrixShift = new Matrix4x4();
        matrixShift.m[0][0] = 1 + stretchX;
        matrixShift.m[1][1] = 1 + stretchY;
        matrixShift.m[2][2] = 1 + stretchZ;
        matrixShift.m[3][3] = 1;
        return matrixShift;
    }

    public static Matrix4x4 getShiftMatrix(double shiftX, double shiftY, double shiftZ){
        Matrix4x4 matrixShift = new Matrix4x4();
        matrixShift.m[0][0] = 1;
        matrixShift.m[3][0] = shiftX;
        matrixShift.m[1][1] = 1;
        matrixShift.m[3][1] = shiftY;
        matrixShift.m[2][2] = 1;
        matrixShift.m[3][2] = shiftZ;
        matrixShift.m[3][3] = 1;
        return matrixShift;
    }


    public static Matrix4x4 getMatrixOne() {
        Matrix4x4 stat = new Matrix4x4();
        stat.m[0][0] = 1;
        stat.m[1][1] = 1;
        stat.m[2][2] = 1;
        stat.m[3][3] = 1;

        return stat;
    }

    public static Matrix4x4 getRotationMatrixX(double angle) {
        Matrix4x4 xRotation = new Matrix4x4();
        xRotation.m[0][0] = 1;
        xRotation.m[1][1] = Math.cos(angle);
        xRotation.m[1][2] = Math.sin(angle);
        xRotation.m[2][1] = -Math.sin(angle);
        xRotation.m[2][2] = Math.cos(angle);
        xRotation.m[3][3] = 1;
        return xRotation;
    }

    public static Matrix4x4 getRotationMatrixY(double angle) {
        Matrix4x4 yRotation = new Matrix4x4();
        yRotation.m[0][0] = Math.cos(angle);
        yRotation.m[0][2] = -Math.sin(angle);
        yRotation.m[1][1] = 1;
        yRotation.m[2][0] = Math.sin(angle);
        yRotation.m[2][2] = Math.cos(angle);
        yRotation.m[3][3] = 1;
        return yRotation;
    }

    public static Matrix4x4 getRotationMatrixZ(double angle) {
        Matrix4x4 zRotation = new Matrix4x4();
        zRotation.m[0][0] = Math.cos(angle);
        zRotation.m[0][1] = Math.sin(angle);
        zRotation.m[1][0] = -Math.sin(angle);
        zRotation.m[1][1] = Math.cos(angle);
        zRotation.m[2][2] = 1;
        zRotation.m[3][3] = 1;
        return zRotation;
    }

    public static Vector3D multiplyByVector(Vector3D v, Matrix4x4 m) {
        double x = v.x * m.m[0][0] + v.y * m.m[1][0] + v.z * m.m[2][0] + m.m[3][0];
        double y = v.x * m.m[0][1] + v.y * m.m[1][1] + v.z * m.m[2][1] + m.m[3][1];
        double z = v.x * m.m[0][2] + v.y * m.m[1][2] + v.z * m.m[2][2] + m.m[3][2];
        double r = v.x * m.m[0][3] + v.y * m.m[1][3] + v.z * m.m[2][3] + m.m[3][3];

        if (r != 0) {
            x /= r;
            y /= r;
            z /= r;
        }

        return new Vector3D(x, y, z);
    }

    private static Matrix4x4 multiply(Matrix4x4 a, Matrix4x4 b) {
        int rowsInA = a.m.length;
        int columnsInA = a.m[0].length; // same as rows in B
        int columnsInB = b.m[0].length;
        double[][] c = new double[rowsInA][columnsInB];
        for (int i = 0; i < rowsInA; i++) {
            for (int j = 0; j < columnsInB; j++) {
                for (int k = 0; k < columnsInA; k++) {
                    c[i][j] = c[i][j] + a.m[i][k] * b.m[k][j];
                }
            }
        }
        return new Matrix4x4(c);
    }

    public static Matrix4x4 multiply(Matrix4x4[] matrices){
        Matrix4x4 result = getMatrixOne();
        for (Matrix4x4 matrix : matrices) {
            result = multiply(result, matrix);
        }
        return result;
    }
}