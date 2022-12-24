package en.opengl.com.dimention.two.debug;

import en.opengl.com.drawer.TriangleDrawer;
import en.opengl.com.entity.Polygon3D;
import en.opengl.com.entity.Vector2D;
import en.opengl.com.entity.Vector3D;
import en.opengl.com.params.DotFactory;
import en.opengl.com.params.Projection;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

import static java.awt.Color.WHITE;

public class CordsRenderer {
    public static void renderPolygonCords(List<Polygon3D> polygons, BufferedImage img, Projection d) {
        Graphics2D g2 = img.createGraphics();
        for (Polygon3D polygon : polygons) {
            Vector3D[] vertexes = polygon.vertexes;
            Vector2D[] dots = new Vector2D[3];
            for (int i = 0; i < 3; i++) {
                dots[i] = DotFactory.getDot(d, vertexes[i]);
                g2.drawString(String.format("(%d, %d, %d)", (int)vertexes[i].x, (int)vertexes[i].y, (int)vertexes[i].z), (int) dots[i].x, (int) dots[i].y);
            }
        }
    }
}
