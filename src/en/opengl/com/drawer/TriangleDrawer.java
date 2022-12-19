package en.opengl.com.drawer;

import en.opengl.com.entity.Vector2D;
import en.opengl.com.entity.Vector3D;

import java.awt.*;
import java.awt.image.BufferedImage;

public class TriangleDrawer {
    public static void shape(Vector2D v0, Vector2D v1, Vector2D v2, BufferedImage img, Color color){
        LineDrawer.line(v0, v1, img, color);
        LineDrawer.line(v1, v2, img, color);
        LineDrawer.line(v2, v0, img, color);
    }

    public static void triangle(Vector2D v0, Vector2D v1, Vector2D v2, BufferedImage img, Color color) {
        Vector2D[] dots = new Vector2D[]{v0, v1, v2};
        Vector2D bboxmin = new Vector2D(img.getWidth() - 1, img.getHeight() - 1);
        Vector2D bboxmax = new Vector2D(0, 0);
        Vector2D clamp = new Vector2D(img.getWidth() - 1, img.getHeight() - 1);
        for (int i = 0; i < 3; i++) {
            bboxmin.x = Math.max(0, Math.min(bboxmin.x, dots[i].x));
            bboxmin.y = Math.max(0, Math.min(bboxmin.y, dots[i].y));

            bboxmax.x = Math.min(clamp.x, Math.max(bboxmax.x, dots[i].x));
            bboxmax.y = Math.min(clamp.y, Math.max(bboxmax.y, dots[i].y));
        }
        Vector2D P = new Vector2D(bboxmin.x, bboxmin.y);
        for (P.x = bboxmin.x; P.x <= bboxmax.x; P.x++) {
            for (P.y = bboxmin.y; P.y <= bboxmax.y; P.y++) {
                Vector3D bc_screen = barycentric(dots, P);
                if (bc_screen.x < 0 || bc_screen.y < 0 || bc_screen.z < 0) continue;
                img.setRGB((int) P.x, (int) P.y, color.getRGB());
            }
        }
    }

    private static Vector3D barycentric(Vector2D[] dots, Vector2D P) {
        Vector3D u = Vector3D.cross(
                new Vector3D(dots[2].x - dots[0].x, dots[1].x - dots[0].x, dots[0].x - P.x),
                new Vector3D(dots[2].y - dots[0].y, dots[1].y - dots[0].y, dots[0].y - P.y));
        if (Math.abs(u.z) < 1) return new Vector3D(-1, 1, 1);
        return new Vector3D(1.f - (u.x + u.y) / u.z, u.y / u.z, u.x / u.z);
    }
}
