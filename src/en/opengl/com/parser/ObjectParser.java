package en.opengl.com.parser;

import en.opengl.com.entity.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ObjectParser {
    public static Obj3D parseObj(String path, String texturePath, int w, int h) {
        List<Vector3D> vertexes = new ArrayList<>();
        List<Texture> textures = new ArrayList<>();
        List<Normal> normals = new ArrayList<>();
        List<Face> faces = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            while (reader.ready()) {
                String replace = reader.readLine().replace("  ", " ");
                String[] line = replace.split(" ");

                if (line[0].equals("v")) {
                    vertexes.add(new Vector3D(
                            Double.parseDouble(line[1]) * 1000,
                            Double.parseDouble(line[2]) * 1000,
                            Double.parseDouble(line[3]) * 1000));
                }

                if (line[0].equals("vt")) {
                    textures.add(new Texture(
                            Double.parseDouble(line[1]),
                            Double.parseDouble(line[2])));
                }

                if (line[0].equals("vn")) {
                    normals.add(new Normal(
                            Double.parseDouble(line[1]),
                            Double.parseDouble(line[2]),
                            Double.parseDouble(line[3])));
                }

                if (line[0].equals("f")) {
                    String[] f0 = line[1].split("/");
                    String[] f1 = line[2].split("/");
                    String[] f2 = line[3].split("/");

                    int v0 = Integer.parseInt(f0[0]) - 1;
                    int v1 = Integer.parseInt(f1[0]) - 1;
                    int v2 = Integer.parseInt(f2[0]) - 1;

                    int vt0 = -1;
                    int vt1 = -1;
                    int vt2 = -1;

                    int vn0 = -1;
                    int vn1 = -1;
                    int vn2 = -1;

                    if (f0.length == 2) {
                        vt0 = Integer.parseInt(f0[1]) - 1;
                        vt1 = Integer.parseInt(f1[1]) - 1;
                        vt2 = Integer.parseInt(f2[1]) - 1;
                    }

                    if (f0.length == 3) {
                        if (!f0[1].equals("")) vt0 = Integer.parseInt(f0[1]) - 1;
                        if (!f1[1].equals("")) vt1 = Integer.parseInt(f1[1]) - 1;
                        if (!f2[1].equals("")) vt2 = Integer.parseInt(f2[1]) - 1;

                        vn0 = Integer.parseInt(f0[2]) - 1;
                        vn1 = Integer.parseInt(f1[2]) - 1;
                        vn2 = Integer.parseInt(f2[2]) - 1;
                    }

                    faces.add(new Face(v0, v1, v2, vt0, vt1, vt2, vn0, vn1, vn2));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        double maxX = Double.MIN_VALUE, maxY = Double.MIN_VALUE, maxZ = Double.MIN_VALUE, minX = Double.MAX_VALUE, minY = Double.MAX_VALUE, minZ = Double.MAX_VALUE;

        for (Vector3D vertex : vertexes) {
            if (vertex.x > maxX) maxX = vertex.x;
            if (vertex.y > maxY) maxY = vertex.y;
            if (vertex.z > maxZ) maxZ = vertex.z;
            if (vertex.x < minX) minX = vertex.x;
            if (vertex.y < minY) minY = vertex.y;
            if (vertex.z < minZ) minZ = vertex.z;
        }

        double dx = (maxX - minX);
        double dy = (maxY - minY);
        double dz = (maxZ - minZ);

        for (Vector3D vertex : vertexes) vertex.x -= minX;
        for (Vector3D vertex : vertexes) vertex.y -= minY;
        for (Vector3D vertex : vertexes) vertex.z -= minZ;

        double scale = Math.min(w, h) / Math.max(dy, dx);

        for (Vector3D vertex : vertexes) {
            vertex.x = vertex.x * scale;
            vertex.y = vertex.y * scale;
            vertex.z = vertex.z * scale;
        }

        BufferedImage texture;
        try {
            texture = ImageIO.read(new File(texturePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new Obj3D(Polygon3D.createPolygons(vertexes, textures, normals, faces),
                textures, dx * scale, dy * scale, dz * scale, texture);
    }
}
