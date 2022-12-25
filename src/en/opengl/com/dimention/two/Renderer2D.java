package en.opengl.com.dimention.two;

import en.opengl.com.drawer.TriangleDrawer;
import en.opengl.com.entity.Matrix4x4;
import en.opengl.com.entity.Polygon3D;
import en.opengl.com.entity.Vector2D;
import en.opengl.com.entity.Vector3D;
import en.opengl.com.params.DotFactory;
import en.opengl.com.params.Projection;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

import static en.opengl.com.params.Properties.h;
import static en.opengl.com.params.Properties.w;
import static java.awt.Color.WHITE;

public class Renderer2D{
    public static List<Polygon3D> render(List<Polygon3D> polygons, BufferedImage img, Projection d, int x, int y, int z, Matrix4x4 mResult, boolean shape, boolean fill, boolean light, boolean cords) {
        Vector3D lightPosition = new Vector3D(x, y, z);
        Vector3D camera = new Vector3D(w/2.0, h/2.0, -10000);
        Random r = new Random();
        Graphics2D g2 = (Graphics2D) img.getGraphics();
        List<Polygon3D> polygonsTransformed = new ArrayList<>();

        for (Polygon3D polygon : polygons) {
            Vector3D[] vertexes = polygon.vertexes;
            Vector3D[] vertexesTransformed = new Vector3D[3];

            for (int i = 0; i < 3; i++) {
                vertexesTransformed[i] = Matrix4x4.multiplyByVector(vertexes[i], mResult);
            }
            polygonsTransformed.add(new Polygon3D(vertexesTransformed, polygon.textures, polygon.normals));
        }

        Polygon3D.sortPolygonsReverse(polygonsTransformed);

        for (Polygon3D polygon : polygonsTransformed) {
            Vector3D[] vertexes = polygon.vertexes;
            Vector2D[] dots = new Vector2D[3];

            for (int i = 0; i < 3; i++) {
                dots[i] = DotFactory.getDot(d, vertexes[i]);
            }

            Vector3D lightCords = Vector3D.diff(lightPosition, vertexes[0]);

            Vector3D n = Vector3D.cross(Vector3D.diff(vertexes[2], vertexes[0]), Vector3D.diff(vertexes[1], vertexes[0]));

            double intensity = Math.acos(Vector3D.dot(n, lightCords) / (n.length() * lightCords.length())) / (Math.PI / 2) - 1;
            if (intensity < 0) intensity = 0;

            double similarity = Vector3D.dot(n.normalize(), Vector3D.diff(vertexes[0], camera));

            if (similarity < 0) {
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
                        dots[i] = DotFactory.getDot(d, vertexes[i]);
                        g2.drawString(String.format("(%d, %d, %d)", (int) vertexes[i].x, (int) vertexes[i].y, (int) vertexes[i].z), (int) dots[i].x, (int) dots[i].y);
                    }
                }
            }
        }
        return polygonsTransformed;
    }
}

