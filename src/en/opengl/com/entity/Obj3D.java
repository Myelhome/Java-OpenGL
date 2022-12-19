package en.opengl.com.entity;

import java.util.List;

public class Obj3D {
    public List<Polygon3D> polygons;
    public List<Texture> textures;

    public double dx;
    public double dy;
    public double dz;

    public Obj3D(List<Polygon3D> polygons, List<Texture> textures, double dx, double dy, double dz) {
        this.polygons = polygons;
        this.textures = textures;
        this.dx = dx;
        this.dy = dy;
        this.dz = dz;
    }
}
