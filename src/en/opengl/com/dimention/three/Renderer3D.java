package en.opengl.com.dimention.three;

import en.opengl.com.drawer.TriangleDrawer;
import en.opengl.com.entity.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static en.opengl.com.params.Properties.h;
import static java.awt.Color.WHITE;

public class Renderer3D {
    public static List<Polygon3D> render(List<Polygon3D> polygons, double[] zBuffer, BufferedImage img, int x, int y, int z, Matrix4x4 mRot, Matrix4x4 mProj, boolean shape, boolean textured, boolean light, boolean cords, BufferedImage texture) {
        Vector3D lightPosition = new Vector3D(x, y, z);
        int tWidth = texture.getWidth();
        int tHeight = texture.getHeight();
        Graphics2D g2 = (Graphics2D) img.getGraphics();
        List<Polygon3D> polygonsTransformed = new ArrayList<>();

        for (Polygon3D polygon : polygons) {
            Texture[] textures = polygon.textures;
            Vector3D[] vertexes = new Vector3D[3];
            Vector3D[] dots = new Vector3D[3];

            for (int i = 0; i < 3; i++) {
                vertexes[i] = Matrix4x4.multiplyByVector(polygon.vertexes[i], mRot);
                vertexes[i] = Matrix4x4.multiplyByVector(vertexes[i], mProj);
                dots[i] = new Vector3D(vertexes[i].x, h - vertexes[i].y, vertexes[i].z, textures[i].u * tWidth, tHeight - (textures[i].v) * tHeight);
            }

            Vector3D lightCords = Vector3D.diff(lightPosition, vertexes[0]);

            Vector3D n = Vector3D.cross(Vector3D.diff(vertexes[2], vertexes[0]), Vector3D.diff(vertexes[1], vertexes[0]));

            double intensity = Math.acos(Vector3D.dot(n, lightCords) / (n.length() * lightCords.length())) / (Math.PI / 2) - 1;
            if (intensity < 0) intensity = 0;

            if (textured) {
                TriangleDrawer.triangleTextured(dots[0], dots[1], dots[2], img, zBuffer, intensity, texture);
            }

            if (light) {
                TriangleDrawer.triangleZBuffer(dots[0], dots[1], dots[2], img, zBuffer,
                        new Color((int) (255 * intensity), (int) (255 * intensity), (int) (255 * intensity)));
            }

            if (shape) {
                TriangleDrawer.shape(
                        Vector2D.toVector2D(dots[0]),
                        Vector2D.toVector2D(dots[1]),
                        Vector2D.toVector2D(dots[2]),
                        img, WHITE);
            }

            if (cords) {
                for (int i = 0; i < 3; i++) {
                    g2.drawString(String.format("(%d, %d, %d)", (int) vertexes[i].x, (int) vertexes[i].y, (int) vertexes[i].z), (int) vertexes[i].x, h - (int) vertexes[i].y);
                }
            }
            polygonsTransformed.add(new Polygon3D(vertexes, polygon.textures, polygon.normals));
        }
        return polygonsTransformed;
    }
}
