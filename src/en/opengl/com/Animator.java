package en.opengl.com;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import static en.opengl.com.params.Properties.*;

public class Animator extends JFrame{
    private static void draw(BufferedImage img) {
    }

    public static void main(String[] args) throws InterruptedException {
        Animator jf = new Animator();
        jf.setSize(w, h);
        jf.setUndecorated(false);
        jf.setVisible(true);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.createBufferStrategy(2);
        jf.setBackground(Color.BLACK);
        BufferedImage img;

        while (true) {
            long rate = framerate;
            long start = System.currentTimeMillis();
            BufferStrategy bs = jf.getBufferStrategy();
            Graphics2D g = (Graphics2D) bs.getDrawGraphics();
            g.clearRect(0, 0, jf.getWidth(), jf.getHeight());
            img = new BufferedImage(w + 1, h + 1, BufferedImage.TYPE_INT_ARGB);

            draw(img);

            g.drawImage(img, 0, 0, null);

            bs.show();
            g.dispose();

            long end = System.currentTimeMillis();
            long len = end - start;
            if (len < rate) {
                Thread.sleep(rate - len);
            }
        }

    }
}
