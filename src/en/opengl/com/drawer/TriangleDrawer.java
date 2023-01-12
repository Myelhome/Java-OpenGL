package en.opengl.com.drawer;

import en.opengl.com.entity.Vector2D;
import en.opengl.com.entity.Vector3D;

import java.awt.*;
import java.awt.image.BufferedImage;

import static en.opengl.com.params.Properties.w;

public class TriangleDrawer {
    public static void shape(Vector2D v0, Vector2D v1, Vector2D v2, BufferedImage img, Color color) {
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
        for (P.x = (int) bboxmin.x; P.x <= bboxmax.x; P.x++) {
            for (P.y = (int) bboxmin.y; P.y <= bboxmax.y; P.y++) {
                Vector3D bc_screen = barycentric(dots, P);
                if (bc_screen.x < 0 || bc_screen.y < 0 || bc_screen.z < 0) continue;
                img.setRGB((int) P.x, (int) P.y, color.getRGB());
            }
        }
    }

    public static void triangleZBuffer(Vector3D v0, Vector3D v1, Vector3D v2, BufferedImage img, double[] zBuffer, Color color) {
        Vector3D[] dots = new Vector3D[]{v0, v1, v2};
        Vector2D bboxmin = new Vector2D(img.getWidth() - 1, img.getHeight() - 1);
        Vector2D bboxmax = new Vector2D(0, 0);
        Vector2D clamp = new Vector2D(img.getWidth() - 1, img.getHeight() - 1);
        for (int i = 0; i < 3; i++) {
            bboxmin.x = Math.max(0, Math.min(bboxmin.x, dots[i].x));
            bboxmin.y = Math.max(0, Math.min(bboxmin.y, dots[i].y));

            bboxmax.x = Math.min(clamp.x, Math.max(bboxmax.x, dots[i].x));
            bboxmax.y = Math.min(clamp.y, Math.max(bboxmax.y, dots[i].y));
        }
        Vector3D P = new Vector3D();
        for (P.x = (int) bboxmin.x; P.x <= bboxmax.x; P.x++) {
            for (P.y = (int) bboxmin.y; P.y <= bboxmax.y; P.y++) {
                Vector3D bc_screen = barycentric(dots, P);
                if (bc_screen.x < 0 || bc_screen.y < 0 || bc_screen.z < 0) continue;
                P.z = 0;
                P.z += dots[0].z * bc_screen.x;
                P.z += dots[1].z * bc_screen.y;
                P.z += dots[2].z * bc_screen.z;
                if (P.x + P.y * w < zBuffer.length && zBuffer[(int) (P.x + P.y * w)] < P.z) {
                    zBuffer[(int) (P.x + P.y * w)] = P.z;
                    img.setRGB((int) P.x, (int) P.y, color.getRGB());
                }
            }
        }
    }

    public static void triangleTextured(Vector3D v0, Vector3D v1, Vector3D v2, BufferedImage img, double[] zBuffer, double intensity, BufferedImage texture) {
        Vector3D[] dots = new Vector3D[]{v0, v1, v2};
        Vector3D bboxmin = new Vector3D(img.getWidth() - 1, img.getHeight() - 1, -1, texture.getWidth(), texture.getHeight());
        Vector3D bboxmax = new Vector3D(0, 0, -1, 0, 0);
        Vector3D clamp = new Vector3D(img.getWidth() - 1, img.getHeight() - 1, -1, texture.getWidth(), texture.getHeight());

        for (int i = 0; i < 3; i++) {
            bboxmin.x = Math.max(0, Math.min(bboxmin.x, dots[i].x));
            bboxmin.y = Math.max(0, Math.min(bboxmin.y, dots[i].y));
            bboxmin.u = Math.max(0, Math.min(bboxmin.u, dots[i].u));
            bboxmin.v = Math.max(0, Math.min(bboxmin.v, dots[i].v));

            bboxmax.x = Math.min(clamp.x, Math.max(bboxmax.x, dots[i].x));
            bboxmax.y = Math.min(clamp.y, Math.max(bboxmax.y, dots[i].y));
            bboxmax.u = Math.min(clamp.u, Math.max(bboxmax.u, dots[i].u));
            bboxmax.v = Math.min(clamp.v, Math.max(bboxmax.v, dots[i].v));
        }

        double dx = bboxmax.x - bboxmin.x;
        double dy = bboxmax.y - bboxmin.y;
        double du = bboxmax.u - bboxmin.u;
        double dv = bboxmax.v - bboxmin.v;

        Vector3D P = new Vector3D();
        for (P.x = (int) bboxmin.x; P.x <= bboxmax.x; P.x++) {
            for (P.y = (int) bboxmin.y; P.y <= bboxmax.y; P.y++) {
                Vector3D bc_screen = barycentric(dots, P);
                if (bc_screen.x < 0 || bc_screen.y < 0 || bc_screen.z < 0) continue;
                P.z = 0;
                P.z += dots[0].z * bc_screen.x;
                P.z += dots[1].z * bc_screen.y;
                P.z += dots[2].z * bc_screen.z;
                if (P.x + P.y * w < zBuffer.length && zBuffer[(int) (P.x + P.y * w)] < P.z) {
                    zBuffer[(int) (P.x + P.y * w)] = P.z;
                    Color color = new Color(texture.getRGB(
                            (int) (bboxmin.u + (P.x - bboxmin.x) * du / dx),
                            (int) (bboxmin.v + (P.y - bboxmin.y) * dv / dy)));
                    img.setRGB((int) P.x, (int) P.y, new Color((int) (color.getRed() * intensity), (int) (color.getGreen() * intensity), (int) (color.getBlue() * intensity)).getRGB());
                }
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

    private static Vector3D barycentric(Vector3D[] dots, Vector3D P) {
        Vector3D u = Vector3D.cross(
                new Vector3D(dots[2].x - dots[0].x, dots[1].x - dots[0].x, dots[0].x - P.x),
                new Vector3D(dots[2].y - dots[0].y, dots[1].y - dots[0].y, dots[0].y - P.y));
        if (Math.abs(u.z) < 1) return new Vector3D(-1, 1, 1);
        return new Vector3D(1.f - (u.x + u.y) / u.z, u.y / u.z, u.x / u.z);
    }
}
