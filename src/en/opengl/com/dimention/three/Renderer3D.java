package en.opengl.com.dimention.three;

import en.opengl.com.drawer.TriangleDrawer;
import en.opengl.com.entity.Matrix4x4;
import en.opengl.com.entity.Polygon3D;
import en.opengl.com.entity.Vector2D;
import en.opengl.com.entity.Vector3D;
import en.opengl.com.params.DotFactory;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static en.opengl.com.params.Properties.h;
import static en.opengl.com.params.Properties.w;
import static java.awt.Color.WHITE;

public class Renderer3D {
    public static List<Polygon3D> render(List<Polygon3D> polygons, BufferedImage img, int x, int y, int z, Matrix4x4 mRot, Matrix4x4 mProj, boolean shape, boolean fill, boolean light, boolean cords) {
        Vector3D camera = new Vector3D(0, 0, -1);
        Vector3D lights = new Vector3D(x, y, z);
        Graphics2D g2 = (Graphics2D) img.getGraphics();
        Random r = new Random();
        List<Polygon3D> translatedPolygons = new ArrayList<>();

        //rotation and transform
        for (Polygon3D polygon : polygons) {
            Vector3D[] vertexes = polygon.vertexes;
            Vector3D[] translation = new Vector3D[3];

            for (int i = 0; i < 3; i++) {
                translation[i] = Matrix4x4.multiplyByVector(vertexes[i], mRot);
            }
            translatedPolygons.add(new Polygon3D(translation, polygon.textures, polygon.normals));
        }

        //sorting
        Polygon3D.sortPolygons(translatedPolygons);

        //projecting
        for (Polygon3D polygon : translatedPolygons) {
            Vector3D[] projection = new Vector3D[3];
            Vector3D[] vertexes = polygon.vertexes;
            Vector2D[] dots = new Vector2D[3];

            for (int i = 0; i < 3; i++) {
                projection[i] = Matrix4x4.multiplyByVector(vertexes[i], mProj);
                projection[i].x = (projection[i].x + 1.0) * 0.5 * w;
                projection[i].y = (projection[i].y + 1.0) * 0.5 * h;
                dots[i] = new Vector2D((int) projection[i].x, (int) (h - projection[i].y));
            }

            Vector3D lightCords = Vector3D.diff(lights, vertexes[0]);

            Vector3D n = Vector3D.cross(Vector3D.diff(vertexes[2], vertexes[0]), Vector3D.diff(vertexes[1], vertexes[0]));

            double intensity = 1 - (Math.acos(Vector3D.dot(n, lightCords) / (n.length() * lightCords.length())) / (Math.PI / 2));
            if (intensity < 0) intensity = 0;

            double similarity = Vector3D.dot(n, Vector3D.diff(vertexes[0], camera));

            if (similarity > 0) {
                if (fill) {
                    TriangleDrawer.triangle(dots[0], dots[1], dots[2], img, new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
                }

                if (light) {
                    TriangleDrawer.triangle(dots[0], dots[1], dots[2], img, new Color((int) (255 * intensity), (int) (255 * intensity), (int) (255 * intensity)));
                }

                if (shape) {
                    TriangleDrawer.shape(dots[0], dots[1], dots[2], img, WHITE);
                }

                if (cords) {
                    for (int i = 0; i < 3; i++) {
                        g2.drawString(String.format("(%d, %d, %d)", (int) vertexes[i].x, (int) vertexes[i].y, (int) vertexes[i].z), (int) dots[i].x, (int) dots[i].y);
                    }
                }
            }
        }
        return translatedPolygons;
    }
}
