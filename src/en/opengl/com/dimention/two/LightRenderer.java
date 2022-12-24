package en.opengl.com.dimention.two;

import en.opengl.com.drawer.TriangleDrawer;
import en.opengl.com.entity.Polygon3D;
import en.opengl.com.entity.Vector2D;
import en.opengl.com.entity.Vector3D;
import en.opengl.com.params.DotFactory;
import en.opengl.com.params.Projection;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class LightRenderer {
    public static void renderPolygonLight(List<Polygon3D> polygons, BufferedImage img, Projection d, int x, int y, int z) {
        Vector3D light = new Vector3D(x, y, z);
        for (Polygon3D polygon : polygons) {
            Vector3D[] vertexes = polygon.vertexes;
            Vector2D[] dots = new Vector2D[3];

            for (int i = 0; i < 3; i++) {
                dots[i] = DotFactory.getDot(d, vertexes[i]);
            }

            Vector3D lightCords = Vector3D.diff(light, vertexes[0]);

            Vector3D n = Vector3D.cross(Vector3D.diff(vertexes[2], vertexes[0]), Vector3D.diff(vertexes[1], vertexes[0]));

            double intensity = Math.acos(Vector3D.dot(n, lightCords) / (n.length() * lightCords.length())) / (Math.PI / 2) - 1;
            if (intensity < 0) intensity = 0;

            TriangleDrawer.triangle(
                    dots[0],
                    dots[1],
                    dots[2],
                    img,
                    new Color((int) (255 * intensity), (int) (255 * intensity), (int) (255 * intensity)));
        }
    }
}
