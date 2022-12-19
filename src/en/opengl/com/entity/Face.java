package en.opengl.com.entity;

public class Face {
    public final int v0;
    public final int v1;
    public final int v2;
    public final int vt0;
    public final int vt1;
    public final int vt2;
    public final int vn0;
    public final int vn1;
    public final int vn2;

    public Face(int v0, int v1, int v2, int vt0, int vt1, int vt2, int vn0, int vn1, int vn2) {
        this.v0 = v0;
        this.v1 = v1;
        this.v2 = v2;
        this.vt0 = vt0;
        this.vt1 = vt1;
        this.vt2 = vt2;
        this.vn0 = vn0;
        this.vn1 = vn1;
        this.vn2 = vn2;
    }
}
