package en.opengl.com.drawer;

import en.opengl.com.entity.Vector2D;
import en.opengl.com.util.Swap;

import java.awt.*;
import java.awt.image.BufferedImage;

import static en.opengl.com.params.Properties.h;
import static en.opengl.com.params.Properties.w;
import static en.opengl.com.util.Swap.swap;

public class LineDrawer {
    public static void line(Vector2D v0, Vector2D v1, BufferedImage v2, Color color) {
        boolean steep = false;
        int x0 = (int) v0.x, y0 = (int) v0.y, x1 = (int) v1.x, y1 = (int) v1.y;

        if (Math.abs(x0 - x1) < Math.abs(y0 - y1)) {
            x0 = swap(y0, y0 = x0);
            x1 = swap(y1, y1 = x1);
            steep = true;
        }

        if (x0 > x1) {
            x0 = swap(x1, x1 = x0);
            y0 = swap(y1, y1 = y0);
        }

        int dx = x1 - x0;
        int dy = y1 - y0;
        int derror2 = Math.abs(dy) * 2;
        int error2 = 0;
        int y = y0;

        for (int x = x0; x <= x1; x++) {
            int pX = x;
            int pY = y;
            if (steep){
                pX = y;
                pY = x;
            }
            if (pX < w && pY < h && pX >= 0 && pY >= 0) v2.setRGB(pX, pY, color.getRGB());

            error2 += derror2;
            if (error2 > dx) {
                y += (y1 > y0 ? 1 : -1);
                error2 -= dx * 2;
            }
        }
    }
}
