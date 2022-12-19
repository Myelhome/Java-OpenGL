package en.opengl.com.samples;

import en.opengl.com.drawer.LineDrawer;
import en.opengl.com.entity.Vector2D;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class LineTester {
    public static void renderSixteenLines(BufferedImage img) {
        Random r = new Random();

        int centerX = img.getWidth() / 2;
        int centerY = img.getHeight() / 2;

        double angle = Math.PI / 16 * 2;

        int radius = Math.min(centerX, centerY);

        for (int i = 0; i < 16; i++) {
            LineDrawer.line(
                    new Vector2D(centerX, centerY),
                    new Vector2D(
                            (int) (centerX + radius * Math.cos(angle * i)),
                            (int) (centerY + radius * Math.sin(angle * i))),
                    img,
                    new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
        }
    }
}
