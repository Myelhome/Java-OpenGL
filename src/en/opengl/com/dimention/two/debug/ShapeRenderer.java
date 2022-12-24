package en.opengl.com.dimention.two.debug;

import en.opengl.com.drawer.TriangleDrawer;
import en.opengl.com.entity.Polygon3D;
import en.opengl.com.entity.Vector2D;
import en.opengl.com.entity.Vector3D;
import en.opengl.com.params.Projection;
import en.opengl.com.params.DotFactory;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

import static java.awt.Color.WHITE;

public class ShapeRenderer {
    public static void renderPolygonShape(List<Polygon3D> polygons, BufferedImage img, Projection d) {
        for (Polygon3D polygon : polygons) {
            Vector3D[] vertexes = polygon.vertexes;
            Vector2D[] dots = new Vector2D[3];
            for (int i = 0; i < 3; i++) {
                dots[i] = DotFactory.getDot(d, vertexes[i]);
            }
            TriangleDrawer.shape(dots[0], dots[1], dots[2], img, WHITE);
        }
    }
}
