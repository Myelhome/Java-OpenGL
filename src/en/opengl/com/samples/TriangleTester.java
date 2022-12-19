package en.opengl.com.samples;

import en.opengl.com.drawer.TriangleDrawer;
import en.opengl.com.entity.Vector2D;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class TriangleTester {
    public static void renderPolygonesFigures(BufferedImage img) {
        Random r = new Random();

        int[] rect = new int[]{3, 4, 8, 16};

        int[] centerXRect = new int[]{img.getWidth() / 4, img.getWidth() * 3 / 4, img.getWidth() / 4, img.getWidth() * 3 / 4};
        int[] centerYRect = new int[]{img.getHeight() / 4, img.getHeight() / 4, img.getHeight() * 3 / 4, img.getHeight() * 3 / 4};

        int radius = Math.min(img.getWidth() / 2, img.getHeight() / 2) * 3 / 8;

        for (int k = 0; k < rect.length; k++) {

            double angleRect = (Math.PI / rect[k] * 2);
            double additionalAngle = Math.PI / 2;

            int rectCenterX = centerXRect[k];
            int rectCenterY = centerYRect[k];

            int oldX = (int) (rectCenterX + radius * Math.cos(additionalAngle + angleRect * 0));
            int oldY = (int) (rectCenterY + radius * Math.sin(additionalAngle + angleRect * 0));

            for (int i = 1; i < rect[k] + 1; i++) {

                int newX = (int) (rectCenterX + radius * Math.cos(additionalAngle + angleRect * i));
                int newY = (int) (rectCenterY + radius * Math.sin(additionalAngle + angleRect * i));

                TriangleDrawer.triangle(
                        new Vector2D(rectCenterX, rectCenterY),
                        new Vector2D(oldX, oldY),
                        new Vector2D(newX, newY),
                        img,
                        new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
                oldX = newX;
                oldY = newY;
            }
        }
    }
}
