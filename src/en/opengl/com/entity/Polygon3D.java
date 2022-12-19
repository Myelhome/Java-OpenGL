package en.opengl.com.entity;

import java.util.ArrayList;
import java.util.List;

public class Polygon3D {
    public Vector3D[] vertexes;
    public Texture[] textures;
    public Normal[] normals;

    public Polygon3D(Vector3D[] vertexes, Texture[] textures, Normal[] normals) {
        this.vertexes = vertexes;
        this.textures = textures;
        this.normals = normals;
    }

    public static List<Polygon3D> createPolygons(List<Vector3D> vertexes, List<Texture> textures, List<Normal> normals, List<Face> faces) {
        List<Polygon3D> polygons = new ArrayList<>();

        for (Face face : faces) {
            Normal[] normalsArr = new Normal[3];
            Texture[] texturesArr = new Texture[3];
            Vector3D[] vertexesArr = new Vector3D[]{
                    vertexes.get(face.v0),
                    vertexes.get(face.v1),
                    vertexes.get(face.v2)
            };

            if (face.vt0 != -1) {
                texturesArr = new Texture[]{
                        textures.get(face.vt0),
                        textures.get(face.vt1),
                        textures.get(face.vt2)
                };
            }

            if (face.vn0 != -1) {
                normalsArr = new Normal[]{
                        normals.get(face.vn0),
                        normals.get(face.vn1),
                        normals.get(face.vn2),
                };
            }

            polygons.add(new Polygon3D(vertexesArr, texturesArr, normalsArr));
        }

        sortPolygonsReverse(polygons);
        return polygons;
    }

    public static void sortPolygonsReverse(List<Polygon3D> polygons) {
        polygons.sort((p1, p2) -> {
            int MaxZ1 = (int) (Math.max(Math.max(p1.vertexes[0].z, p1.vertexes[1].z), p1.vertexes[2].z) * 1000);
            int MaxZ2 = (int) (Math.max(Math.max(p2.vertexes[0].z, p2.vertexes[1].z), p2.vertexes[2].z) * 1000);
            return MaxZ1 - MaxZ2;
        });
    }

    public static void sortPolygons(List<Polygon3D> polygons) {
        polygons.sort((p1, p2) -> {
            int MaxZ1 = (int) (Math.max(Math.max(p1.vertexes[0].z, p1.vertexes[1].z), p1.vertexes[2].z) * 1000);
            int MaxZ2 = (int) (Math.max(Math.max(p2.vertexes[0].z, p2.vertexes[1].z), p2.vertexes[2].z) * 1000);
            return MaxZ2 - MaxZ1;
        });
    }
}

