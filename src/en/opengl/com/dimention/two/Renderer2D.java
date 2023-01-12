package en.opengl.com.dimention.two;

import en.opengl.com.drawer.TriangleDrawer;
import en.opengl.com.entity.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

import static en.opengl.com.params.Properties.h;
import static en.opengl.com.params.Properties.w;
import static java.awt.Color.WHITE;

public class Renderer2D {

    public static List<Polygon3D> render(List<Polygon3D> polygons, double[] zBuffer, BufferedImage img, int x, int y, int z, Matrix4x4 mResult, boolean shape, boolean textured, boolean light, boolean cords, BufferedImage texture) {
        Vector3D lightPosition = new Vector3D(x, y, z);
        Vector3D camera = new Vector3D(w / 2.0, h / 2.0, -10000);
        int tWidth = texture.getWidth();
        int tHeight = texture.getHeight();
        Graphics2D g2 = (Graphics2D) img.getGraphics();
        List<Polygon3D> polygonsTransformed = new ArrayList<>();

        for (Polygon3D polygon : polygons) {
            Vector3D[] vertexes = polygon.vertexes;
            Texture[] textures = polygon.textures;
            Vector3D[] vertexesTransformed = new Vector3D[3];

            for (int i = 0; i < 3; i++) {
                vertexesTransformed[i] = Matrix4x4.multiplyByVector(vertexes[i], mResult);
            }

            Vector3D lightCords = Vector3D.diff(lightPosition, vertexesTransformed[0]);

            Vector3D n = Vector3D.cross(Vector3D.diff(vertexesTransformed[2], vertexesTransformed[0]), Vector3D.diff(vertexesTransformed[1], vertexesTransformed[0]));

            double intensity = Math.acos(Vector3D.dot(n, lightCords) / (n.length() * lightCords.length())) / (Math.PI / 2) - 1;
            if (intensity < 0) intensity = 0;

            double similarity = Vector3D.dot(n.normalize(), Vector3D.diff(vertexesTransformed[0], camera));

            if (similarity < 0) {
                if (textured) {
                    TriangleDrawer.triangleTextured(
                            new Vector3D(vertexesTransformed[0].x, h - vertexesTransformed[0].y, vertexesTransformed[0].z, textures[0].u * tWidth, tHeight - (textures[0].v) * tHeight),
                            new Vector3D(vertexesTransformed[1].x, h - vertexesTransformed[1].y, vertexesTransformed[1].z, textures[1].u * tWidth, tHeight - (textures[1].v) * tHeight),
                            new Vector3D(vertexesTransformed[2].x, h - vertexesTransformed[2].y, vertexesTransformed[2].z, textures[2].u * tWidth, tHeight - (textures[2].v) * tHeight),
                            img, zBuffer, intensity, texture);
                }

                if (light) {
                    TriangleDrawer.triangleZBuffer(new Vector3D(
                                    vertexesTransformed[0].x, h - vertexesTransformed[0].y, vertexesTransformed[0].z),
                            new Vector3D(vertexesTransformed[1].x, h - vertexesTransformed[1].y, vertexesTransformed[1].z),
                            new Vector3D(vertexesTransformed[2].x, h - vertexesTransformed[2].y, vertexesTransformed[2].z),
                            img, zBuffer, new Color((int) (255 * intensity), (int) (255 * intensity), (int) (255 * intensity)));
                }

                if (shape) {
                    TriangleDrawer.shape(
                            new Vector2D(vertexesTransformed[0].x, h - vertexesTransformed[0].y),
                            new Vector2D(vertexesTransformed[1].x, h - vertexesTransformed[1].y),
                            new Vector2D(vertexesTransformed[2].x, h - vertexesTransformed[2].y),
                            img, WHITE);
                }

                if (cords) {
                    for (int i = 0; i < 3; i++) {
                        g2.drawString(String.format("(%d, %d, %d)", (int) vertexesTransformed[i].x, (int) vertexesTransformed[i].y, (int) vertexesTransformed[i].z), (int) vertexesTransformed[i].x, h - (int) vertexesTransformed[i].y);
                    }
                }
            }
            polygonsTransformed.add(new Polygon3D(vertexesTransformed, polygon.textures, polygon.normals));
        }
        return polygonsTransformed;
    }
}

